package com.lovejazz.gymsession.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class AuthService {
    private final String keycloakClientSecret;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthService(RestTemplate restTemplate,ObjectMapper objectMapper) {
        Dotenv dotenv = Dotenv.load();
       this.keycloakClientSecret = dotenv.get("KEYCLOAK_CLIENT_SECRET");
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String authenticateAndGetToken(String username, String password) {
        String url = "http://localhost:8080/realms/develop/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", "gymsession-rest-api");
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);
        map.add("client_secret", keycloakClientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                String accessToken = root.path("access_token").asText();
                return accessToken;
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse JSON response: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Authentication failed: " + response.getStatusCode());
        }
    }

    public String getUserCreatorToken (){
            String url = "http://localhost:8080/realms/develop/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("client_id", "gymsession-rest-api");
            map.add("grant_type", "client_credentials");
            map.add("client_secret", keycloakClientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                try {
                    JsonNode root = objectMapper.readTree(response.getBody());
                    String accessToken = root.path("access_token").asText();
                    return accessToken;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse JSON response: " + e.getMessage(), e);
                }
            } else {
                throw new RuntimeException("Authentication failed: " + response.getStatusCode());
            }
    }

    public String registerAndGetToken(String username, String email, String firstName, String lastName, String password) throws JsonProcessingException {
        String accessToken = this.getUserCreatorToken();
        String url = "http://localhost:8080/admin/realms/develop/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + accessToken);

        ObjectNode objectNode = objectMapper.createObjectNode()
                .put("username", username)
                .put("email", email)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("enabled", "true");

        ArrayNode credentialsArray = objectMapper.createArrayNode();
        ObjectNode credentialNode = objectMapper.createObjectNode();
        credentialNode.put("type", "password");
        credentialNode.put("value", password);
        credentialNode.put("temporary", "false");
        credentialsArray.add(credentialNode);

        objectNode.set("credentials", credentialsArray);


        String jsonString = objectMapper.writeValueAsString(objectNode);


        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);


        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                return this.authenticateAndGetToken(username, password);
            } catch (Exception authException) {
                throw new RuntimeException("User created, but authentication failed: " + authException.getMessage(), authException);
            }
        } else {
            throw new RuntimeException("User registration failed: " + response.getStatusCode());
        }
    }

    public String authenticateViaGoogleAndGetToken (String googleAccessToken) {
        String url = "http://localhost:8080/realms/develop/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        map.add("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");
        map.add("client_id", "gymsession-rest-api");
        map.add("client_secret", keycloakClientSecret);
        map.add("subject_token", googleAccessToken);
        map.add("subject_issuer", "google");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                String accessToken = root.path("access_token").asText();
                return accessToken;
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse JSON response: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Authentication failed: " + response.getStatusCode());
        }
    }
}
