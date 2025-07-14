package com.ankur.url_shortener.logging;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    @Value("${auth.email}")
    private String email;

    @Value("${auth.name}")
    private String name;

    @Value("${auth.rollNo}")
    private String rollNo;

    @Value("${auth.accessCode}")
    private String accessCode;

    @Value("${auth.clientID}")
    private String clientID;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    private final String authUrl = "http://20.244.56.144/evaluation-service/auth";

    @Getter
    private String accessToken;

    @PostConstruct
    public void fetchToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> authRequest = Map.of(
                "email", email,
                "name", name,
                "rollNo", rollNo,
                "accessCode", accessCode,
                "clientID", clientID,
                "clientSecret", clientSecret
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(authRequest, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
            accessToken = (String) response.getBody().get("access_token");
            log.info("Token fetched successfully");
        } catch (Exception e) {
            log.error("Failed to fetch token: {}", e.getMessage());
        }
    }
}
