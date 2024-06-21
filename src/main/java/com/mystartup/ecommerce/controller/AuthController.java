package com.mystartup.ecommerce.controller;

import com.mystartup.ecommerce.dto.JwtAuthenticationResponse;
import  com.mystartup.ecommerce.dto.LoginRequest;
import  com.mystartup.ecommerce.dto.SignUpRequest;
import  com.mystartup.ecommerce.entity.User;
import  com.mystartup.ecommerce.security.JwtTokenProvider;
import  com.mystartup.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        User user = userService.findByEmail(loginRequest.getEmail());

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getFirstName(),user.getId()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userService.findByEmail(signUpRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setRole("ROLE_USER");
        user.setDni(signUpRequest.getDni());
        user.setAddress(signUpRequest.getAddress());
        user.setCity(signUpRequest.getCity());

        userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
