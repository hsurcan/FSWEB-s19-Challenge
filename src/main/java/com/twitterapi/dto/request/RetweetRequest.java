package com.twitterapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RetweetRequest(
        @NotNull(message = "Retweet edilecek tweet id zorunludur")
        Long tweetId,

        @Size(max = 280, message = "Alinti en fazla 280 karakter olabilir")
        String quote
) {}
