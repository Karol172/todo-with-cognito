package com.kcymerys.java.todowithcognito.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CognitoJWT {

    private String id_token;
    private String access_token;
    private String refresh_token;
    private Long expires_in;
    private String token_type;

}
