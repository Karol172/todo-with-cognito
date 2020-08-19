package com.kcymerys.java.todowithcognito.security.filter;

import com.kcymerys.java.todowithcognito.utils.CognitoProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationFilter extends BasicAuthenticationFilter {

    private static final String AUTH_BEARER_STRING = "Bearer";

    private final RemoteJWKSet<SecurityContext> remoteJWKSet;
    private final CognitoProperties cognitoProperties;

    public AuthenticationFilter(AuthenticationManager authenticationManager, CognitoProperties properties) {
        super(authenticationManager);
        URL url;
        try {
            url = new URL(properties.getKeyStorePath());
        } catch (MalformedURLException e) {
            url = null;
        }
        this.remoteJWKSet = url != null ? new RemoteJWKSet<>(url) : null;
        this.cognitoProperties = properties;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (remoteJWKSet == null || token.isEmpty() || !token.get().contains(AUTH_BEARER_STRING)) {
            return null;
        }
        try {
            JWT jwt = JWTParser.parse(token.get().replace(AUTH_BEARER_STRING, "").trim());
            if (cognitoProperties.getIssuer().equals(jwt.getJWTClaimsSet().getIssuer())) {
                JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, remoteJWKSet);
                ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
                jwtProcessor.setJWSKeySelector(keySelector);
                JWTClaimsSet claimsSet = jwtProcessor.process(jwt, null);

                List<GrantedAuthority> authorities =
                        ((List<String>) claimsSet.getClaim("cognito:groups"))
                                .stream()
                                .map(group -> {
                                    switch (group) {
                                        case "admin":
                                            return new SimpleGrantedAuthority("ROLE_ADMIN");
                                        case "users":
                                            return new SimpleGrantedAuthority("ROLE_USER");
                                        default:
                                            return new SimpleGrantedAuthority("EMPTY");
                                    }
                                })
                                .filter(group -> !group.getAuthority().equals("EMPTY"))
                                .collect(Collectors.toList());

                return new UsernamePasswordAuthenticationToken(
                        claimsSet,
                        null,
                        authorities
                );
            }

        } catch (ParseException | JOSEException | BadJOSEException e) {
            return null;
        }
        return null;
    }

}
