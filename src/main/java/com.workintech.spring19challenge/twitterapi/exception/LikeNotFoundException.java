package com.twitterapi.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(Long tweetId) {
        super("Bu tweet uzerinde size ait bir begeni bulunamadi. tweetId: " + tweetId);
    }
}
