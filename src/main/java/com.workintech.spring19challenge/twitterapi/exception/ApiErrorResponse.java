package com.twitterapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tum hatalarin dondugu standart govde (ornek projedeki ApiResponse karsiligi).
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {}
