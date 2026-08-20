package com.twitterapi.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String content,
        Long userId,
        String username,
        Long tweetId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
