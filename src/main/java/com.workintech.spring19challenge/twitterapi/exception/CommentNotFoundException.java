package com.twitterapi.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Yorum bulunamadi. id: " + id);
    }
}
