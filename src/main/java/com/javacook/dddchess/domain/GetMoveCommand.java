package com.javacook.dddchess.domain;

import java.util.Date;


/**
 * Created by javacook on 21.04.17.
 */
public class GetMoveCommand extends Command {

    public final int moveIndex;

    public GetMoveCommand(int moveIndex) {
        this.moveIndex = moveIndex;
    }

}
