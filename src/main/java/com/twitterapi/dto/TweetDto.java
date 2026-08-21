package com.twitterapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TweetDto(
        Long id,
        String content,
        Long userId,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likeCount,
        long retweetCount,
        long commentCount,
        List<CommentDto> comments
) {}
