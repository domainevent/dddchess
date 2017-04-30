package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;
import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;

import static com.javacook.dddchess.domain.FigureValueObject.ColorEnum.*;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.*;
import static com.javacook.dddchess.domain.PositionValueObject.HorCoord.*;
import static com.javacook.dddchess.domain.PositionValueObject.VertCoord.*;


/**
 * Created by javacook on 30.04.17.
 */
public class ChessBoardValueObject {

    public final static int HOR_SIZE = HorCoord.values().length;
    public final static int VERT_SIZE = VertCoord.values().length;

    FigureValueObject[][] board = new FigureValueObject[HOR_SIZE][VERT_SIZE];
    ColorEnum lastMoveColor;

    public void performMove(MoveValueObject move) throws MoveException {
        final FigureValueObject figureFrom = getFigureAtPosition(move.from);
    }

    FigureValueObject getFigureAtPosition(PositionValueObject position) {
        return board[position.horCoord.ordinal()][position.vertCoord.ordinal()];
    }

    void setFigure(HorCoord horCoord, VertCoord vertCoord, FigureValueObject figure) {
        board[horCoord.ordinal()][vertCoord.ordinal()] = figure;
    }


    /**
     *
     * @param bottomColor Color of the figures at the bottom of the board starting with a1
     */
    void initialize(ColorEnum bottomColor) {
        setFigure(A, _1, new FigureValueObject(ROCK, bottomColor));
        setFigure(B, _1, new FigureValueObject(KNIGHT, bottomColor));
        setFigure(C, _1, new FigureValueObject(BISHOP, bottomColor));
        if (bottomColor == WHITE) {
            setFigure(D, _1, new FigureValueObject(QUEEN, bottomColor));
            setFigure(E, _1, new FigureValueObject(KING, bottomColor));
        }
        else {
            setFigure(E, _1, new FigureValueObject(KING, bottomColor));
            setFigure(D, _1, new FigureValueObject(QUEEN, bottomColor));
        }
        setFigure(F, _1, new FigureValueObject(BISHOP, bottomColor));
        setFigure(G, _1, new FigureValueObject(KNIGHT, bottomColor));
        setFigure(H, _1, new FigureValueObject(ROCK, bottomColor));

        for (HorCoord coord = A; coord.next() != null; coord = coord.next()) {
            setFigure(coord, _2, new FigureValueObject(PAWN, bottomColor));
        }

        ColorEnum topColor = (bottomColor == WHITE)? BLACK : WHITE;

        setFigure(A, _8, new FigureValueObject(ROCK, topColor));
        setFigure(B, _8, new FigureValueObject(KNIGHT, topColor));
        setFigure(C, _8, new FigureValueObject(BISHOP, topColor));
        if (topColor == BLACK) {
            setFigure(D, _8, new FigureValueObject(QUEEN, topColor));
            setFigure(E, _8, new FigureValueObject(KING, topColor));
        }
        else {
            setFigure(E, _8, new FigureValueObject(KING, topColor));
            setFigure(D, _8, new FigureValueObject(QUEEN, topColor));
        }
        setFigure(F, _8, new FigureValueObject(BISHOP, topColor));
        setFigure(G, _8, new FigureValueObject(KNIGHT, topColor));
        setFigure(H, _8, new FigureValueObject(ROCK, topColor));

        for (HorCoord coord = A; coord.next() != null; coord = coord.next()) {
            setFigure(coord, _7, new FigureValueObject(PAWN, topColor));
        }
    }

}
