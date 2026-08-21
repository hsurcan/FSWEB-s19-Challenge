package com.twitterapi.controller;

import com.twitterapi.dto.LikeDto;
import com.twitterapi.dto.request.LikeRequest;
import com.twitterapi.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<LikeDto> like(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeService.likeTweet(request, principal.getUsername()));
    }

    /** POST — daha once atilan begeniyi kaldirir. */
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        likeService.dislikeTweet(request, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
