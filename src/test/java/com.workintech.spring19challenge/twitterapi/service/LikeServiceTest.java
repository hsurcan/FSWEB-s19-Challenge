package com.twitterapi.service;

import com.twitterapi.dto.LikeDto;
import com.twitterapi.dto.converter.LikeDtoConverter;
import com.twitterapi.dto.request.LikeRequest;
import com.twitterapi.entity.Like;
import com.twitterapi.entity.Tweet;
import com.twitterapi.entity.User;
import com.twitterapi.exception.AlreadyExistsException;
import com.twitterapi.exception.LikeNotFoundException;
import com.twitterapi.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TweetService tweetService;

    private LikeService likeService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        likeService = new LikeService(likeRepository, tweetService, new LikeDtoConverter());
        user = User.builder().id(1L).username("begenen").build();
        tweet = Tweet.builder().id(10L).content("Tweet")
                .user(User.builder().id(2L).username("sahip").build()).build();
    }

    @Test
    void likeTweet_shouldSaveLike_whenNotLikedBefore() {
        LikeRequest request = new LikeRequest(10L);
        Like like = Like.builder().id(50L).user(user).tweet(tweet).build();

        when(tweetService.getUserByUsername("begenen")).thenReturn(user);
        when(tweetService.getTweetById(10L)).thenReturn(tweet);
        when(likeRepository.existsByUserIdAndTweetId(1L, 10L)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        LikeDto dto = likeService.likeTweet(request, "begenen");

        assertEquals(50L, dto.id());
        assertEquals(10L, dto.tweetId());
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void likeTweet_shouldThrowConflict_whenAlreadyLiked() {
        LikeRequest request = new LikeRequest(10L);
        when(tweetService.getUserByUsername("begenen")).thenReturn(user);
        when(tweetService.getTweetById(10L)).thenReturn(tweet);
        when(likeRepository.existsByUserIdAndTweetId(1L, 10L)).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> likeService.likeTweet(request, "begenen"));
        verify(likeRepository, never()).save(any());
    }

    @Test
    void dislikeTweet_shouldDeleteLike_whenLikeExists() {
        LikeRequest request = new LikeRequest(10L);
        Like like = Like.builder().id(50L).user(user).tweet(tweet).build();

        when(tweetService.getUserByUsername("begenen")).thenReturn(user);
        when(tweetService.getTweetById(10L)).thenReturn(tweet);
        when(likeRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.of(like));

        likeService.dislikeTweet(request, "begenen");

        verify(likeRepository, times(1)).delete(like);
    }

    @Test
    void dislikeTweet_shouldThrowNotFound_whenLikeDoesNotExist() {
        LikeRequest request = new LikeRequest(10L);
        when(tweetService.getUserByUsername("begenen")).thenReturn(user);
        when(tweetService.getTweetById(10L)).thenReturn(tweet);
        when(likeRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.empty());

        assertThrows(LikeNotFoundException.class,
                () -> likeService.dislikeTweet(request, "begenen"));
        verify(likeRepository, never()).delete(any());
    }
}
