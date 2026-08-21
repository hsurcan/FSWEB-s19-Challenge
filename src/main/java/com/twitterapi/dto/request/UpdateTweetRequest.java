package com.twitterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTweetRequest(
        @NotBlank(message = "Tweet icerigi bos olamaz")
        @Size(max = 280, message = "Tweet en fazla 280 karakter olabilir")
        String content
) {}
