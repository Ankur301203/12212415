package com.ankur.logging.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
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

    @Getter
    private String accessToken;

    public TokenService() {
        fetchToken();
    }

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
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://20.244.56.144/evaluation-service/auth", request, Map.class);
            accessToken = (String) response.getBody().get("access_token");
            log.info("Token fetched successfully.");
        } catch (Exception e) {
            log.error("Failed to fetch token: {}", e.getMessage());
        }
    }
}
