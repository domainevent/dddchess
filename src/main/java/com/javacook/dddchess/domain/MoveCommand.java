package com.javacook.dddchess.domain;

import java.util.Date;

/**
 * Created by javacook on 21.04.17.
 */
public class MoveCommand {

    public final MoveValueObject move;
    public final Date occuredOn = new Date();

    public MoveCommand(MoveValueObject move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "MoveCommand{" +
                "move=" + move +
                ", occuredOn=" + occuredOn +
                '}';
    }
}
