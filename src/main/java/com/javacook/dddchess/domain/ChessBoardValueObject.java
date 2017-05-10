package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;


/**
 * Created by vollmer on 05.05.17.
 */
public class ChessBoardValueObject extends ValueObject {

    protected final FigureValueObject[][] board;

    /**
     * Default constructor
     */
    public ChessBoardValueObject() {
        this(new FigureValueObject[HorCoord.values().length][VertCoord.values().length]);
    }

    /**
     * Copy Constructor
     */
    public ChessBoardValueObject(ChessBoardValueObject toCopy) {
        this(toCopy.getBoard());
    }

    protected ChessBoardValueObject(FigureValueObject[][] board) {
        this.board = board;
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

    protected void setFigureToPosition(PositionValueObject position, FigureValueObject figur) {
        board[position.horCoord.ordinal()][position.vertCoord.ordinal()] = figur;
    }

    protected void setFigureToPosition(HorCoord h, VertCoord v,
                                            FigureValueObject.FigureEnum figur, ColorEnum color) {
        setFigureToPosition(new PositionValueObject(h,v), new FigureValueObject(figur, color));
    }

    protected FigureValueObject getFigureAtPosition(PositionValueObject position) {
        return board[position.horCoord.ordinal()][position.vertCoord.ordinal()];
    }

    @Override
    public String toString() {
        return ChessBoardEntity.boardToString(board);
    }

}
