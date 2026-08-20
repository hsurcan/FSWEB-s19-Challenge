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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetService tweetService;
    private final LikeDtoConverter likeDtoConverter;

    /**
     * POST /like : kimligi dogrulanmis kullanici tweeti begenir.
     * Ayni tweete ikinci like -> 409 (veritabaninda da UNIQUE ile korunur).
     */
    @Transactional
    public LikeDto likeTweet(LikeRequest request, String username) {
        User user = tweetService.getUserByUsername(username);
        Tweet tweet = tweetService.getTweetById(request.tweetId());

        if (likeRepository.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new AlreadyExistsException("Bu tweeti zaten begendiniz");
        }

        Like like = Like.builder()
                .user(user)
                .tweet(tweet)
                .build();

        return likeDtoConverter.convert(likeRepository.save(like));
    }

    /**
     * POST /dislike : kullanicinin bu tweet uzerindeki begenisini kaldirir.
     * Begeni yoksa 404.
     */
    @Transactional
    public void dislikeTweet(LikeRequest request, String username) {
        User user = tweetService.getUserByUsername(username);
        Tweet tweet = tweetService.getTweetById(request.tweetId());

        Like like = likeRepository.findByUserIdAndTweetId(user.getId(), tweet.getId())
                .orElseThrow(() -> new LikeNotFoundException(tweet.getId()));

        likeRepository.delete(like);
    }
}
