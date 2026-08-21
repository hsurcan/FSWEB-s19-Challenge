package com.twitterapi.dto;

import java.time.LocalDateTime;

public record RetweetDto(
        Long id,
        String quote,
        Long userId,
        String username,
        Long tweetId,
        LocalDateTime createdAt
) {}
