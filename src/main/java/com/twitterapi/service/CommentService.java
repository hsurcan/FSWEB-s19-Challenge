package com.twitterapi.service;

import com.twitterapi.dto.CommentDto;
import com.twitterapi.dto.converter.CommentDtoConverter;
import com.twitterapi.dto.request.CreateCommentRequest;
import com.twitterapi.dto.request.UpdateCommentRequest;
import com.twitterapi.entity.Comment;
import com.twitterapi.entity.Tweet;
import com.twitterapi.entity.User;
import com.twitterapi.exception.CommentNotFoundException;
import com.twitterapi.exception.UnauthorizedActionException;
import com.twitterapi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetService tweetService;
    private final CommentDtoConverter commentDtoConverter;

    /**POST: bir tweete, kimligi dogrulanmis kullanici adina yorum.
     */
    @Transactional
    public CommentDto createComment(CreateCommentRequest request, String username) {
        User user = tweetService.getUserByUsername(username);
        Tweet tweet = tweetService.getTweetById(request.tweetId());

        Comment comment = Comment.builder()
                .content(request.content())
                .user(user)
                .tweet(tweet)
                .build();

        return commentDtoConverter.convert(commentRepository.save(comment));
    }

    /**PUT: yalnizca yorumun sahibi guncelleyebilir.
     */
    @Transactional
    public CommentDto updateComment(Long id, UpdateCommentRequest request, String username) {
        Comment comment = getCommentById(id);

        if (!comment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Sadece yorum sahibi yorumu guncelleyebilir");
        }

        comment.setContent(request.content());
        return commentDtoConverter.convert(commentRepository.save(comment));
    }

    /**DELETE: yorum sahibi VEYA tweet sahibi silebilir.
     */
    @Transactional
    public void deleteComment(Long id, String username) {
        Comment comment = getCommentById(id);

        boolean isCommentOwner = comment.getUser().getUsername().equals(username);
        boolean isTweetOwner = comment.getTweet().getUser().getUsername().equals(username);

        if (!isCommentOwner && !isTweetOwner) {
            throw new UnauthorizedActionException(
                    "Yorumu sadece yorum sahibi veya tweet sahibi silebilir");
        }

        commentRepository.delete(comment);
    }

    private Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}
