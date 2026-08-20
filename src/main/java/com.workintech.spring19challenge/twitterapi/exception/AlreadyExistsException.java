package com.twitterapi.exception;

/**
 * Mukerrer kayit durumlari: ayni username/email ile kayit,
 * ayni tweete ikinci like veya ikinci retweet.
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
