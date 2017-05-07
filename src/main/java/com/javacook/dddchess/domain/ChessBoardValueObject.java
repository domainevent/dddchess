package com.javacook.dddchess.domain;

import static com.javacook.dddchess.domain.PositionValueObject.VertCoord._1;


/**
 * Created by vollmer on 05.05.17.
 */
public class ChessBoardValueObject extends ValueObject {

    public final FigureValueObject[][] board;

    public ChessBoardValueObject(FigureValueObject[][] board) {
        this.board = board;
    }

    public ChessBoardValueObject() {
        this(null);
    }

    @Override
    public String toString() {
        return ChessBoardEntity.boardToString(board);
    }

}
