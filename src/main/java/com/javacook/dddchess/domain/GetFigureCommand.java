package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.Command;
import com.javacook.dddchess.domain.PositionValueObject;


/**
 * Created by vollmer on 08.05.17.
 */
public class GetFigureCommand extends Command {

    public final PositionValueObject position;

    public GetFigureCommand(PositionValueObject position) {
        this.position = position;
    }
}
