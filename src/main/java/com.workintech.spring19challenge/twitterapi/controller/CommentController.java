package com.twitterapi.controller;

import com.twitterapi.dto.CommentDto;
import com.twitterapi.dto.request.CreateCommentRequest;
import com.twitterapi.dto.request.UpdateCommentRequest;
import com.twitterapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /** POST /comment — bir tweete yorum yazar. */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(request, principal.getUsername()));
    }

    /** PUT /comment/{id} — sadece yorum sahibi. */
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(
                commentService.updateComment(id, request, principal.getUsername()));
    }

    /** DELETE /comment/{id} — yorum sahibi VEYA tweet sahibi. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        commentService.deleteComment(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
