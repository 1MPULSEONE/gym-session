package com.lovejazz.gymsession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lovejazz.gymsession.controller.TestAuthController;
import com.lovejazz.gymsession.payload.request.SignUpRequest;
import com.lovejazz.gymsession.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestAuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private TestAuthController testAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_ShouldReturnSuccessResponse() throws JsonProcessingException {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("john123");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setEmail("john.doe@example.com");
        signUpRequest.setPassword("password123");

        String mockToken = "mockToken123";
        when(authService.registerAndGetToken(
                "john123",
                "john.doe@example.com",
                "John",
                "Doe",
                "password123"
        )).thenReturn(mockToken);

        // Act
        ResponseEntity<?> response = testAuthController.signup(signUpRequest, httpServletResponse);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Authentication successful", responseBody.get("message"));
        assertEquals(mockToken, responseBody.get("token"));

        verify(httpServletResponse).addCookie(any(Cookie.class));
        verify(authService).registerAndGetToken(
                "john123",
                "john.doe@example.com",
                "John",
                "Doe",
                "password123"
        );
    }

    @Test
    void signup_ShouldReturnErrorResponseWhenRegistrationFails() throws JsonProcessingException {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("john123");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setEmail("john.doe@example.com");
        signUpRequest.setPassword("password123");

        when(authService.registerAndGetToken(any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Registration failed"));

        // Act
        ResponseEntity<?> response = testAuthController.signup(signUpRequest, httpServletResponse);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        Map<String, Object> errorBody = (Map<String, Object>) response.getBody();
        assertNotNull(errorBody);
        assertEquals("Authentication failed", errorBody.get("error"));
        assertEquals("Registration failed", errorBody.get("message"));
    }
}