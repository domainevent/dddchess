package com.javacook.dddchess.domain;

/**
 * Created by vollmer on 05.05.17.
 */
public class ChessBoardValueObject {

    public final FigureValueObject[][] board;


    public ChessBoardValueObject(FigureValueObject[][] board) {
        this.board = board;
    }
}
