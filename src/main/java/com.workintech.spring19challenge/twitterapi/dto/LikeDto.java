package com.twitterapi.dto;

import java.time.LocalDateTime;

public record LikeDto(
        Long id,
        Long userId,
        String username,
        Long tweetId,
        LocalDateTime createdAt
) {}
