package com.lovejazz.gymsession.controller;


import com.lovejazz.gymsession.payload.request.LoginRequest;
import com.lovejazz.gymsession.payload.response.JwtResponse;
import com.lovejazz.gymsession.security.jwt.JwtUtils;
import com.lovejazz.gymsession.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController(
            AuthenticationManager authenticationManager,
            PasswordEncoder encoder,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    //NOTE: Temp solution for generate hashed password
    @GetMapping("/encode")
    public String EndcodedPasword(@Valid @RequestBody Password password) {
        return encoder.encode(password.password());
    }

}
