package com.twitterapi.exception;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(Long id) {
        super("Tweet bulunamadi. id: " + id);
    }
}
