package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.PositionValueObject.*;

import java.util.Arrays;


/**
 * Created by vollmer on 05.05.17.
 */
public class ChessBoardValueObject extends ValueObject {

    protected final FigureValueObject[][] board;

    public ChessBoardValueObject(FigureValueObject[][] board) {
        this.board = board;
    }

    public ChessBoardValueObject() {
        this(new FigureValueObject[HorCoord.values().length][VertCoord.values().length]);
    }

    public FigureValueObject[][] getBoard() {
        final FigureValueObject[][] copy =
                new FigureValueObject[HorCoord.values().length][VertCoord.values().length];
        for (int i = 0; i < HorCoord.values().length; i++) {
            for (int j = 0; j < VertCoord.values().length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }


    @Override
    public String toString() {
        return ChessBoardEntity.boardToString(board);
    }

}
