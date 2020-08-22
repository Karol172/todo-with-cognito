package com.kcymerys.java.todowithcognito.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cognito")
@Data
public class CognitoProperties {

    private String urlRoot;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String issuer;
    private String keyStorePath;

}
