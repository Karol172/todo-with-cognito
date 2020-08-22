package com.kcymerys.java.todowithcognito.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User's tokens.")
public class CognitoJWT {

    @ApiModelProperty(notes = "ID Token.")
    private String id_token;

    @ApiModelProperty(notes = "Access Token.")
    private String access_token;

    @ApiModelProperty(notes = "Refresh Token.")
    private String refresh_token;

    @ApiModelProperty(notes = "Token expiration time.")
    private Long expires_in;

    @ApiModelProperty(notes = "Type of token.")
    private String token_type;

}
