package com.ankur.url_shortener.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortcode;

    private Instant createdAt;
    private Instant expiry;

    private Long clickCount;

    @ElementCollection
    private List<String> clickMetadata = new ArrayList<>();
}

