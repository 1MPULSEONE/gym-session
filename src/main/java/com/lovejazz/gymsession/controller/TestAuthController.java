package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.exception.UserAlreadyExistsException;
import com.lovejazz.gymsession.payload.request.LoginRequest;
import com.lovejazz.gymsession.payload.request.LoginViaGoogleRequest;
import com.lovejazz.gymsession.payload.request.SignUpRequest;
import com.lovejazz.gymsession.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/keycloak_auth")
public class TestAuthController {
    private final AuthService authService;

    public TestAuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            System.out.println("TEST CONTROLLER2");

            String token = authService.authenticateAndGetToken(loginRequest.getUsername(),loginRequest.getPassword());

            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");


            response.addCookie(cookie);


            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Authentication successful");
            responseBody.put("token", token);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Authentication failed");
            errorBody.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }
    }

    @PostMapping("/token")
    public  ResponseEntity<?> token(HttpServletResponse response) {
        try {
            String token = authService.getUserCreatorToken();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            return ResponseEntity.ok(responseBody);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Authentication failed");
            errorBody.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        try {
            String token = authService.registerAndGetToken(signUpRequest.getUsername(),signUpRequest.getEmail(), signUpRequest.getFirstName(),signUpRequest.getLastName(),signUpRequest.getPassword());

            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");


            response.addCookie(cookie);


            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Authentication successful");
            responseBody.put("token", token);

            return ResponseEntity.ok(responseBody);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Authentication failed");
            errorBody.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }
    }

    @PostMapping("/signin-via-google")
    public ResponseEntity<?> signinViaGoogle(@RequestBody LoginViaGoogleRequest loginViaGoogleRequest, HttpServletResponse response) {
        try {
            String token = authService.authenticateViaGoogleAndGetToken(loginViaGoogleRequest.getGoogleAccessToken());

            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");


            response.addCookie(cookie);


            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Authentication successful or user already existed");
            responseBody.put("token", token);

            return ResponseEntity.ok(responseBody);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Authentication or processing failed");
            errorBody.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "Conflict");
        errorBody.put("message", ex.getMessage());
        return errorBody;
    }
}
