package com.javacook.dddchess.domain;

import java.util.Date;

/**
 * Created by javacook on 21.04.17.
 */
public class MoveCommand extends Command {

    public final MoveValueObject move;

    public MoveCommand(MoveValueObject move) {
        this.move = move;
    }

}
