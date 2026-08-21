package com.twitterapi.dto.converter;

import com.twitterapi.dto.LikeDto;
import com.twitterapi.entity.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeDtoConverter {

    public LikeDto convert(Like like) {
        return new LikeDto(
                like.getId(),
                like.getUser().getId(),
                like.getUser().getUsername(),
                like.getTweet().getId(),
                like.getCreatedAt()
        );
    }
}
