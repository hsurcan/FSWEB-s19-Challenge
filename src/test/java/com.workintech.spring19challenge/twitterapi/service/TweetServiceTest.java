package com.twitterapi.service;

import com.twitterapi.dto.TweetDto;
import com.twitterapi.dto.converter.CommentDtoConverter;
import com.twitterapi.dto.converter.TweetDtoConverter;
import com.twitterapi.dto.request.CreateTweetRequest;
import com.twitterapi.dto.request.UpdateTweetRequest;
import com.twitterapi.entity.Tweet;
import com.twitterapi.entity.User;
import com.twitterapi.exception.TweetNotFoundException;
import com.twitterapi.exception.UnauthorizedActionException;
import com.twitterapi.exception.UserNotFoundException;
import com.twitterapi.repository.TweetRepository;
import com.twitterapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    private TweetService tweetService;

    private User owner;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        // Converter gercek nesne olarak verilir: donusum mantigi da test kapsaminda.
        TweetDtoConverter converter = new TweetDtoConverter(new CommentDtoConverter());
        tweetService = new TweetService(tweetRepository, userRepository, converter);

        owner = User.builder()
                .id(1L)
                .firstName("Ali").lastName("Veli")
                .username("aliveli")
                .email("ali@test.com")
                .password("hashed")
                .build();

        tweet = Tweet.builder()
                .id(10L)
                .content("Ilk tweetim")
                .user(owner)
                .build();
    }

    @Test
    void createTweet_shouldSaveAndReturnDto_whenUserExists() {
        CreateTweetRequest request = new CreateTweetRequest("Ilk tweetim");
        when(userRepository.findByUsername("aliveli")).thenReturn(Optional.of(owner));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        TweetDto dto = tweetService.createTweet(request, "aliveli");

        assertNotNull(dto);
        assertEquals("Ilk tweetim", dto.content());
        assertEquals(1L, dto.userId());
        assertEquals("aliveli", dto.username());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void createTweet_shouldThrowUserNotFound_whenUserDoesNotExist() {
        CreateTweetRequest request = new CreateTweetRequest("Icerik");
        when(userRepository.findByUsername("yok")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> tweetService.createTweet(request, "yok"));
        verify(tweetRepository, never()).save(any());
    }

    @Test
    void findByUserId_shouldReturnTweets_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(tweetRepository.findAllByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(tweet));

        List<TweetDto> result = tweetService.findByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).id());
    }

    @Test
    void findByUserId_shouldThrowUserNotFound_whenUserDoesNotExist() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> tweetService.findByUserId(99L));
    }

    @Test
    void findById_shouldReturnDtoWithComments_whenExists() {
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        TweetDto dto = tweetService.findById(10L);

        assertEquals(10L, dto.id());
        assertNotNull(dto.comments()); // findById tum bilgileri (yorumlar dahil) doner
    }

    @Test
    void findById_shouldThrowTweetNotFound_whenNotExists() {
        when(tweetRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TweetNotFoundException.class, () -> tweetService.findById(99L));
    }

    @Test
    void updateTweet_shouldUpdate_whenCallerIsOwner() {
        UpdateTweetRequest request = new UpdateTweetRequest("Guncellenmis icerik");
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(inv -> inv.getArgument(0));

        TweetDto dto = tweetService.updateTweet(10L, request, "aliveli");

        assertEquals("Guncellenmis icerik", dto.content());
    }

    @Test
    void updateTweet_shouldThrowUnauthorized_whenCallerIsNotOwner() {
        UpdateTweetRequest request = new UpdateTweetRequest("Baskasinin denemesi");
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        assertThrows(UnauthorizedActionException.class,
                () -> tweetService.updateTweet(10L, request, "davetsiz"));
        verify(tweetRepository, never()).save(any());
    }

    @Test
    void deleteTweet_shouldDelete_whenCallerIsOwner() {
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        tweetService.deleteTweet(10L, "aliveli");

        verify(tweetRepository, times(1)).delete(tweet);
    }

    @Test
    void deleteTweet_shouldThrowUnauthorized_whenCallerIsNotOwner() {
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        assertThrows(UnauthorizedActionException.class,
                () -> tweetService.deleteTweet(10L, "davetsiz"));
        verify(tweetRepository, never()).delete(any());
    }
}
