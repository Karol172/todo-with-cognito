package com.kcymerys.java.todowithcognito.service;

import com.kcymerys.java.todowithcognito.security.model.CognitoJWT;
import com.kcymerys.java.todowithcognito.utils.CognitoProperties;
import com.kcymerys.java.todowithcognito.utils.CognitoRequestHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

    private final CognitoProperties properties;
    private final RestTemplate restTemplate;

    public ResponseEntity login() {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, CognitoRequestHelper.loginUrl(properties))
                .build();
    }

    public CognitoJWT token(String code) {
        return restTemplate.postForEntity(CognitoRequestHelper.tokenUrl(properties, code),
                                            CognitoRequestHelper.buildTokenRequest(properties),
                                            CognitoJWT.class)
                .getBody();
    }

}
