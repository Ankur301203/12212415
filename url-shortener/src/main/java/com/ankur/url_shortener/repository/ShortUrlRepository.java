package com.ankur.url_shortener.repository;

import com.ankur.url_shortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortcode(String shortcode);
    boolean existsByShortcode(String shortcode);
}

