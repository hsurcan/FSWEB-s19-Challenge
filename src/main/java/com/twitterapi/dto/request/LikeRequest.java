package com.twitterapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull(message = "Begenilecek tweet id zorunludur")
        Long tweetId
) {}
