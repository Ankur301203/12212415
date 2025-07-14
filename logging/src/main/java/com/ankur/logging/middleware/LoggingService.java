package com.ankur.logging.middleware;

import com.ankur.logging.auth.TokenService;
import com.ankur.logging.model.LogRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class LoggingService {

    private final TokenService tokenService;

    @Value("${log.api.url}")
    private String logApiUrl;

    public LoggingService(TokenService tokenService){
        this.tokenService = tokenService;
    }

    public void log(String stack, String level, String packageName, String message){
        LogRequest request = new LogRequest();
        request.setStack(stack.toLowerCase());
        request.setLevel(level.toLowerCase());
        request.setPackageName(packageName.toLowerCase());
        request.setMessage(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenService.getAccessToken());

        HttpEntity<LogRequest> entity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    logApiUrl, HttpMethod.POST,entity,String.class);
            System.out.println("Log API Response: "+response.getBody());
        }
        catch(Exception e){
            System.err.println("Log failed: " + e.getMessage());
        }
    }
}
