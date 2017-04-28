package com.javacook.dddchess.domain;

import java.util.Date;

/**
 * Created by vollmer on 21.04.17.
 */
public class MovedEvent {

    public final MoveValueObject move;
    public final Date occuredOn = new Date();

    public MovedEvent(MoveValueObject move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "MoveEvent{" +
                "move=" + move +
                ", occuredOn=" + occuredOn +
                '}';
    }
}
