package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;
import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.javacook.dddchess.domain.FigureValueObject.ColorEnum.*;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.*;
import static com.javacook.dddchess.domain.PositionValueObject.HorCoord.*;
import static com.javacook.dddchess.domain.PositionValueObject.VertCoord.*;


/**
 * Created by javacook on 30.04.17.
 */
public class ChessBoardEntity {

    public final static int HOR_SIZE = HorCoord.values().length;
    public final static int VERT_SIZE = VertCoord.values().length;

    List<MoveValueObject> moves = new ArrayList<>();
    FigureValueObject[][] board = new FigureValueObject[HOR_SIZE][VERT_SIZE];
    ColorEnum lastMoveColor;


    public Optional<MoveValueObject> getMove(int moveIndex) {
        return Optional.ofNullable(
        (moveIndex >= 0 && moveIndex < moves.size())?  moves.get(moveIndex) : null);
    }

    public int performMove(MoveValueObject move) throws MoveException {
        final Optional<FigureValueObject> figureFrom = getFigureAtPosition(move.from);
        if (!figureFrom.isPresent()) {
            throw new MoveException("No figure on " + move.from + " present");
        }
        else if (figureFrom.get().color == lastMoveColor) {
            throw new MoveException("Invalid player: " + lastMoveColor);
        }
        // end validation
        setFigure(move.from, null);
        setFigure(move.to, figureFrom.get());
        lastMoveColor = figureFrom.get().color;
        moves.add(move);
        System.out.println("Color " + lastMoveColor);
        System.out.println(printBoard());
        return moves.size();
    }

    Optional<FigureValueObject> getFigureAtPosition(PositionValueObject position) {
        return getFigureAtPosition(position.horCoord, position.vertCoord);
    }

    Optional<FigureValueObject> getFigureAtPosition(HorCoord horCoord, VertCoord vertCoord) {
        return Optional.ofNullable(board[horCoord.ordinal()][vertCoord.ordinal()]);
    }

    void setFigure(PositionValueObject position, FigureValueObject figure) {
        setFigure(position.horCoord, position.vertCoord, figure);
    }

    void setFigure(HorCoord horCoord, VertCoord vertCoord, FigureValueObject figure) {
        board[horCoord.ordinal()][vertCoord.ordinal()] = figure;
    }


    /**
     *
     * @param bottomColor Color of the figures at the bottom of the board starting with a1
     */
    public void initialize(ColorEnum bottomColor) {
        moves = new ArrayList<>();
        lastMoveColor = null;
        for (VertCoord vertCoord : VertCoord.values()) {
            for (HorCoord horCoord : HorCoord.values()) {
                setFigure(horCoord, vertCoord, null);
            }
        }
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

        for (HorCoord coord : HorCoord.values()) {
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

        for (HorCoord coord : HorCoord.values()) {
            setFigure(coord, _7, new FigureValueObject(PAWN, topColor));
        }
    }

    public String printBoard() {
        final String horLine = "-------------------------";
        String boardAsStr = horLine + System.lineSeparator();
        for(VertCoord vertCoord : VertCoord.valuesInverted()) {
            boardAsStr += "|";
            for(HorCoord horCoord : HorCoord.values()) {
                final Optional<FigureValueObject> figure = getFigureAtPosition(horCoord, vertCoord);
                boardAsStr += figure.isPresent()? figure.get().abbreviation() : "  ";
                boardAsStr += "|";
            }
            boardAsStr += System.lineSeparator() + horLine;
            if (vertCoord != _1) {
                boardAsStr += System.lineSeparator();
            }
        }
        return boardAsStr;
    }

}
