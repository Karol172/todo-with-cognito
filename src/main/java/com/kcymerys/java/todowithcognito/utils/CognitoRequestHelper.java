package com.kcymerys.java.todowithcognito.utils;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Base64;

public class CognitoRequestHelper {

    public static String loginUrl(CognitoProperties properties) {
        return properties.getUrlRoot() + "/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=" + properties.getClientId() +
                "&redirect_uri=" + properties.getRedirectUri() +
                "&scope=openid";
    }

    public static String tokenUrl(CognitoProperties properties, String code) {
        return properties.getUrlRoot() + "/oauth2/token" +
                "?grant_type=authorization_code" +
                "&client_id=" + properties.getClientId() +
                "&code=" + code +
                "&scope=openid"+
                "&redirect_uri=" + properties.getRedirectUri();
    }

    public static HttpEntity buildTokenRequest(CognitoProperties properties) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        headers.add("Authorization", "Basic " +
                Base64.getEncoder()
                        .encodeToString((properties.getClientId() + ':' + properties.getClientSecret())
                                .getBytes()));
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        return new HttpEntity(null, headers);
    }

}
