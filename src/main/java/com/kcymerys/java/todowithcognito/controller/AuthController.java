package com.kcymerys.java.todowithcognito.controller;

import com.kcymerys.java.todowithcognito.security.model.CognitoJWT;
import com.kcymerys.java.todowithcognito.service.AuthService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Api(value = "Auth", description = "API for logging to AWS Cognito", tags = "Auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ApiOperation(value = "Retrieve login form.")
    @ApiResponses(@ApiResponse(code = 303, message = "The request successfully sent location of redirection."))
    public ResponseEntity<?> login () {
        return authService.login();
    }

    @GetMapping("/token")
    @ApiOperation(value = "Retrieve user token. (Callback URI for AWS Cognito)")
    @ApiResponses(@ApiResponse(code = 200, message = "The request was successfully processed."))
    public CognitoJWT token(@ApiParam(value = "Authentication code.", required = true)
                                @RequestParam("code") String code) {
        return authService.token(code);
    }

}
