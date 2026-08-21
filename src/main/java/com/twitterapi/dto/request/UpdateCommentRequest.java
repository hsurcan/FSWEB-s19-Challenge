package com.twitterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequest(
        @NotBlank(message = "Yorum icerigi bos olamaz")
        @Size(max = 280, message = "Yorum en fazla 280 karakter olabilir")
        String content
) {}
