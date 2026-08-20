package com.twitterapi.controller;

import com.twitterapi.dto.RetweetDto;
import com.twitterapi.dto.request.RetweetRequest;
import com.twitterapi.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Direktifte zorunlu olmayan, ornek proje yapisina uyum icin eklenen bonus uc.
 */
@RestController
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    /** POST /retweet — bir tweeti (istege bagli alintiyla) retweet eder. */
    @PostMapping("/retweet")
    public ResponseEntity<RetweetDto> retweet(
            @Valid @RequestBody RetweetRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(retweetService.retweet(request, principal.getUsername()));
    }
}
