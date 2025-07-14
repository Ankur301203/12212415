package com.ankur.url_shortener.service;

import com.ankur.url_shortener.model.ShortUrl;
import com.ankur.url_shortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository repository;

    public ShortUrl createShortUrl(String url, Integer validityMins, String desiredShortcode) {
        String shortcode = desiredShortcode != null && !desiredShortcode.isBlank()
                ? desiredShortcode
                : generateRandomShortcode();

        if (repository.existsByShortcode(shortcode)) {
            throw new RuntimeException("Shortcode already in use");
        }

        Instant now = Instant.now();
        Instant expiry = now.plus(validityMins != null ? validityMins : 30, ChronoUnit.MINUTES);

        ShortUrl entity = ShortUrl.builder()
                .originalUrl(url)
                .shortcode(shortcode)
                .createdAt(now)
                .expiry(expiry)
                .clickCount(0L)
                .build();

        return repository.save(entity);
    }

    public Optional<ShortUrl> getByShortcode(String shortcode) {
        return repository.findByShortcode(shortcode);
    }

    public void incrementClickCount(ShortUrl shortUrl, String clickInfo) {
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrl.getClickMetadata().add(clickInfo);
        repository.save(shortUrl);
    }

    private String generateRandomShortcode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

