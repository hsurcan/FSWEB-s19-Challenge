package com.twitterapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Kullanici bulunamadi. id: " + id);
    }
    public UserNotFoundException(String username) {
        super("Kullanici bulunamadi. username: " + username);
    }
}
