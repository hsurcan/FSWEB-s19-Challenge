package com.twitterapi.dto.converter;

import com.twitterapi.dto.TweetDto;
import com.twitterapi.entity.Tweet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TweetDtoConverter {

    private final CommentDtoConverter commentDtoConverter;

    public TweetDto convert(Tweet tweet) {
        return convert(tweet, false);
    }

    public TweetDto convert(Tweet tweet, boolean withComments) {
        return new TweetDto(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername(),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt(),
                tweet.getLikes() == null ? 0 : tweet.getLikes().size(),
                tweet.getRetweets() == null ? 0 : tweet.getRetweets().size(),
                tweet.getComments() == null ? 0 : tweet.getComments().size(),
                withComments && tweet.getComments() != null
                        ? tweet.getComments().stream().map(commentDtoConverter::convert).toList()
                        : List.of()
        );
    }
}
