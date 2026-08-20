package com.twitterapi.service;

import com.twitterapi.dto.RetweetDto;
import com.twitterapi.dto.converter.RetweetDtoConverter;
import com.twitterapi.dto.request.RetweetRequest;
import com.twitterapi.entity.Retweet;
import com.twitterapi.entity.Tweet;
import com.twitterapi.entity.User;
import com.twitterapi.exception.AlreadyExistsException;
import com.twitterapi.repository.RetweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Direktifte retweet endpointi zorunlu degildir; ornek projede
 * RetweetController bulundugu icin ayni yapiya sadik kalinarak eklenmistir.
 */
@Service
@RequiredArgsConstructor
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetService tweetService;
    private final RetweetDtoConverter retweetDtoConverter;

    @Transactional
    public RetweetDto retweet(RetweetRequest request, String username) {
        User user = tweetService.getUserByUsername(username);
        Tweet tweet = tweetService.getTweetById(request.tweetId());

        if (retweetRepository.existsByUserIdAndTweetId(user.getId(), tweet.getId())) {
            throw new AlreadyExistsException("Bu tweeti zaten retweet ettiniz");
        }

        Retweet retweet = Retweet.builder()
                .quote(request.quote())
                .user(user)
                .tweet(tweet)
                .build();

        return retweetDtoConverter.convert(retweetRepository.save(retweet));
    }
}
