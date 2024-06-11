package com.app.controllers;

import com.app.dto.AuthCreateUserRequest;
import com.app.dto.AuthLoginRequest;
import com.app.dto.AuthResponse;
import com.app.services.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return new ResponseEntity<>(userDetailsServiceImp.loginUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/sing-up")
    public ResponseEntity<AuthResponse> sing(@RequestBody @Valid AuthCreateUserRequest request) {
        return new ResponseEntity<>(userDetailsServiceImp.createuser(request), HttpStatus.CREATED);
    }

}
