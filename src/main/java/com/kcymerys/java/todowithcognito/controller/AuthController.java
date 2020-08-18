package com.kcymerys.java.todowithcognito.controller;

import com.kcymerys.java.todowithcognito.security.model.CognitoJWT;
import com.kcymerys.java.todowithcognito.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public ResponseEntity<?> login () {
        return authService.login();
    }

    @GetMapping("/token")
    public CognitoJWT token(@RequestParam("code") String code) {
        return authService.token(code);
    }

}
