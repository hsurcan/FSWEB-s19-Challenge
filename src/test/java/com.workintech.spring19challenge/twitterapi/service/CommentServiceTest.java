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
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetService tweetService;

    private CommentService commentService;

    private User tweetOwner;
    private User commenter;
    private Tweet tweet;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(
                commentRepository, tweetService, new CommentDtoConverter());

        tweetOwner = User.builder().id(1L).username("tweetsahibi").build();
        commenter = User.builder().id(2L).username("yorumcu").build();

        tweet = Tweet.builder().id(10L).content("Ana tweet").user(tweetOwner).build();
        comment = Comment.builder().id(100L).content("Ilk yorum")
                .user(commenter).tweet(tweet).build();
    }

    @Test
    void createComment_shouldSaveAndReturnDto() {
        CreateCommentRequest request = new CreateCommentRequest(10L, "Ilk yorum");
        when(tweetService.getUserByUsername("yorumcu")).thenReturn(commenter);
        when(tweetService.getTweetById(10L)).thenReturn(tweet);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto dto = commentService.createComment(request, "yorumcu");

        assertEquals("Ilk yorum", dto.content());
        assertEquals(10L, dto.tweetId());
        assertEquals("yorumcu", dto.username());
    }

    @Test
    void updateComment_shouldUpdate_whenCallerIsCommentOwner() {
        UpdateCommentRequest request = new UpdateCommentRequest("Duzeltilmis yorum");
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        CommentDto dto = commentService.updateComment(100L, request, "yorumcu");

        assertEquals("Duzeltilmis yorum", dto.content());
    }

    @Test
    void updateComment_shouldThrowUnauthorized_whenCallerIsNotCommentOwner() {
        UpdateCommentRequest request = new UpdateCommentRequest("Yetkisiz deneme");
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        // Tweet sahibi bile olsa yorumu GUNCELLEYEMEZ (sadece silebilir).
        assertThrows(UnauthorizedActionException.class,
                () -> commentService.updateComment(100L, request, "tweetsahibi"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment_shouldDelete_whenCallerIsCommentOwner() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(100L, "yorumcu");

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_shouldDelete_whenCallerIsTweetOwner() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        // Direktif kurali: tweet sahibi de yorumu silebilir.
        commentService.deleteComment(100L, "tweetsahibi");

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_shouldThrowUnauthorized_whenCallerIsThirdParty() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedActionException.class,
                () -> commentService.deleteComment(100L, "ucuncusahis"));
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteComment_shouldThrowNotFound_whenCommentDoesNotExist() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.deleteComment(999L, "yorumcu"));
    }
}
