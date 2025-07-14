package com.ankur.url_shortener.controller;

import com.ankur.url_shortener.logging.LoggingService;
import com.ankur.url_shortener.model.ShortUrl;
import com.ankur.url_shortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shorturls")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final LoggingService loggingService;

    @PostMapping
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, Object> body) {
        String url = (String) body.get("url");
        Integer validity = body.get("validity") != null ? (Integer) body.get("validity") : null;
        String shortcode = (String) body.get("shortcode");
        ShortUrl created = shortUrlService.createShortUrl(url, validity, shortcode);
        loggingService.log("backend", "info", "controller", "Short URL created: " + created.getShortcode());

        Map<String, Object> response = new HashMap<>();
        response.put("shortLink", "http://localhost:8080/shorturls/" + created.getShortcode());
        response.put("expiry", DateTimeFormatter.ISO_INSTANT.format(created.getExpiry()));

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<?> redirectOrStats(@PathVariable String shortcode, @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        ShortUrl shortUrl = shortUrlService.getByShortcode(shortcode)
                .orElseThrow(() -> new RuntimeException("Shortcode not found"));

        if(shortUrl.getExpiry().isBefore(java.time.Instant.now())){
            return ResponseEntity.status(410).body("Short URL expired");
        }
        shortUrlService.incrementClickCount(shortUrl, "Clicked by: " + userAgent);
        loggingService.log("backend", "info", "controller", "Redirected to: " + shortUrl.getOriginalUrl());
        return ResponseEntity.status(302).location(URI.create(shortUrl.getOriginalUrl())).build();
    }

    @GetMapping("/{shortcode}/stats")
    public ResponseEntity<?> getStats(@PathVariable String shortcode) {
        ShortUrl shortUrl = shortUrlService.getByShortcode(shortcode)
                .orElseThrow(() -> new RuntimeException("Shortcode not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("originalUrl", shortUrl.getOriginalUrl());
        response.put("createdAt", shortUrl.getCreatedAt());
        response.put("expiry", shortUrl.getExpiry());
        response.put("clickCount", shortUrl.getClickCount());
        response.put("clicks", shortUrl.getClickMetadata());

        return ResponseEntity.ok(response);
    }
}

