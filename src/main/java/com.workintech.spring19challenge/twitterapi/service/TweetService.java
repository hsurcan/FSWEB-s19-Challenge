package com.twitterapi.service;

import com.twitterapi.dto.TweetDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetDtoConverter tweetDtoConverter;

    /**
     * Tweet, JWT'den gelen (SecurityContext'teki) kullanici adina olusturulur.
     * Anonim tweet mumkun degildir.
     */
    @Transactional
    public TweetDto createTweet(CreateTweetRequest request, String username) {
        User user = getUserByUsername(username);

        Tweet tweet = Tweet.builder()
                .content(request.content())
                .user(user)
                .build();

        return tweetDtoConverter.convert(tweetRepository.save(tweet));
    }

    @Transactional(readOnly = true)
    public List<TweetDto> findByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return tweetRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(tweetDtoConverter::convert)
                .toList();
    }

    /**
     * findById: tweetin TUM bilgileri — yorum listesi ve sayaclar dahil.
     */
    @Transactional(readOnly = true)
    public TweetDto findById(Long id) {
        return tweetDtoConverter.convert(getTweetById(id), true);
    }

    /**
     * Guncelleme yalnizca tweet sahibine aciktir (silme kuraliyla ayni ilke).
     */
    @Transactional
    public TweetDto updateTweet(Long id, UpdateTweetRequest request, String username) {
        Tweet tweet = getTweetById(id);

        if (!tweet.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Sadece tweet sahibi tweeti guncelleyebilir");
        }

        tweet.setContent(request.content());
        return tweetDtoConverter.convert(tweetRepository.save(tweet));
    }

    /**
     * Sadece tweet sahibi ilgili tweeti silebilir.
     */
    @Transactional
    public void deleteTweet(Long id, String username) {
        Tweet tweet = getTweetById(id);

        if (!tweet.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Sadece tweet sahibi tweeti silebilir");
        }

        tweetRepository.delete(tweet);
    }

    // ---- Diger servislerin de kullandigi yardimcilar ----

    protected Tweet getTweetById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException(id));
    }

    protected User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
