package com.twitterapi.dto.converter;

import com.twitterapi.dto.RetweetDto;
import com.twitterapi.entity.Retweet;
import org.springframework.stereotype.Component;

@Component
public class RetweetDtoConverter {

    public RetweetDto convert(Retweet retweet) {
        return new RetweetDto(
                retweet.getId(),
                retweet.getQuote(),
                retweet.getUser().getId(),
                retweet.getUser().getUsername(),
                retweet.getTweet().getId(),
                retweet.getCreatedAt()
        );
    }
}
