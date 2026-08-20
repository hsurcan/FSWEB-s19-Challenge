package com.twitterapi.dto;

public record AuthResponseDto(
        String token,
        Long userId,
        String username
) {}
