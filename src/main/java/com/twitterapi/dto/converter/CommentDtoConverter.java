package com.twitterapi.dto.converter;

import com.twitterapi.dto.CommentDto;
import com.twitterapi.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoConverter {

    public CommentDto convert(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getTweet().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
