package com.javacook.dddchess.domain;

import java.util.Date;


/**
 * Created by javacook on 21.04.17.
 */
public class GetMoveCommand {

    public final int moveIndex;
    public final Date occuredOn = new Date();

    public GetMoveCommand(int moveIndex) {
        this.moveIndex = moveIndex;
    }


    @Override
    public String toString() {
        return "GetMoveCommand{" +
                "moveIndex=" + moveIndex +
                ", occuredOn=" + occuredOn +
                '}';
    }
}
