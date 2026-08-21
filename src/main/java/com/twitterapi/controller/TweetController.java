package com.twitterapi.controller;

import com.twitterapi.dto.TweetDto;
import com.twitterapi.dto.request.CreateTweetRequest;
import com.twitterapi.dto.request.UpdateTweetRequest;
import com.twitterapi.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<TweetDto> createTweet(
            @Valid @RequestBody CreateTweetRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tweetService.createTweet(request, principal.getUsername()));
    }

    /** GET — kullanicinin tum tweetleri. */
    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetDto>> findByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(tweetService.findByUserId(userId));
    }

    /** GET — tweetin tum bilgileri (yorumlar + sayaclar). */
    @GetMapping("/findById")
    public ResponseEntity<TweetDto> findById(@RequestParam Long id) {
        return ResponseEntity.ok(tweetService.findById(id));
    }

    /** PUT — sadece sahibi. */
    @PutMapping("/{id}")
    public ResponseEntity<TweetDto> updateTweet(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTweetRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(tweetService.updateTweet(id, request, principal.getUsername()));
    }

    /** DELETE — sadece sahibi. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        tweetService.deleteTweet(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
