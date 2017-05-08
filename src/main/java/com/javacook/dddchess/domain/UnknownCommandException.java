package com.javacook.dddchess.domain;

/**
 * Created by vollmer on 08.05.17.
 */
public class UnknownCommandException extends Exception {

    private final Object command;

    public UnknownCommandException(Object command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return "Unknown command " + command;
    }

}
