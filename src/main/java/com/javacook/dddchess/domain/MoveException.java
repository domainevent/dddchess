package com.javacook.dddchess.domain;

/**
 * Created by javacook on 30.04.17.
 */
public class MoveException extends Exception {

    public MoveException() {
    }

    public MoveException(String message) {
        super(message);
    }
}
