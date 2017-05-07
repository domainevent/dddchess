package com.javacook.dddchess.domain;

import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;
import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;

import java.util.Arrays;
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

    public final GameIdValueObject id;

    protected final FigureValueObject[][] board;

    public ChessBoardEntity(GameIdValueObject id) {
        this.id = id;
        board = new FigureValueObject[HOR_SIZE][VERT_SIZE];
    }


    public int performMove(final MoveValueObject move, final Optional<ColorEnum> lastMoveColor) throws MoveException {
        final Optional<FigureValueObject> figureFrom = getFigureAtPosition(move.from);
        if (!figureFrom.isPresent()) {
            throw new MoveException("No figure on " + move.from + " present");
        }
        else if (lastMoveColor.isPresent() && figureFrom.get().color == lastMoveColor.get()) {
            throw new MoveException("Invalid player: " + lastMoveColor);
        }
        // end validation
        setFigure(move.from, null);
        setFigure(move.to, figureFrom.get());
        System.out.println("Color " + lastMoveColor);
        System.out.println(toString());
        return 0;
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
     */
    public void initialize() {
        for (VertCoord vertCoord : VertCoord.values()) {
            for (HorCoord horCoord : HorCoord.values()) {
                setFigure(horCoord, vertCoord, null);
            }
        }
        setFigure(A, _1, new FigureValueObject(ROOK, WHITE));
        setFigure(B, _1, new FigureValueObject(KNIGHT, WHITE));
        setFigure(C, _1, new FigureValueObject(BISHOP, WHITE));
        setFigure(D, _1, new FigureValueObject(QUEEN, WHITE));
        setFigure(E, _1, new FigureValueObject(KING, WHITE));
        setFigure(F, _1, new FigureValueObject(BISHOP, WHITE));
        setFigure(G, _1, new FigureValueObject(KNIGHT, WHITE));
        setFigure(H, _1, new FigureValueObject(ROOK, WHITE));

        for (HorCoord coord : HorCoord.values()) {
            setFigure(coord, _2, new FigureValueObject(PAWN, WHITE));
        }
        setFigure(A, _8, new FigureValueObject(ROOK, BLACK));
        setFigure(B, _8, new FigureValueObject(KNIGHT, BLACK));
        setFigure(C, _8, new FigureValueObject(BISHOP, BLACK));
        setFigure(D, _8, new FigureValueObject(QUEEN, BLACK));
        setFigure(E, _8, new FigureValueObject(KING, BLACK));
        setFigure(F, _8, new FigureValueObject(BISHOP, BLACK));
        setFigure(G, _8, new FigureValueObject(KNIGHT, BLACK));
        setFigure(H, _8, new FigureValueObject(ROOK, BLACK));

        for (HorCoord coord : HorCoord.values()) {
            setFigure(coord, _7, new FigureValueObject(PAWN, BLACK));
        }
    }


    @Override
    public String toString() {
        return "ChessBoardEntity{" +
                "id=" + id +
                ", board=" + System.lineSeparator() + boardToString(board) +
                '}';
    }


    public static String boardToString(FigureValueObject[][] board) {
        final String horLine = "-------------------------";
        String boardAsStr = horLine + System.lineSeparator();
        for(VertCoord vertCoord : VertCoord.valuesInverted()) {
            boardAsStr += "|";
            for(HorCoord horCoord : HorCoord.values()) {
                final FigureValueObject figure = board[horCoord.ordinal()][vertCoord.ordinal()];
                boardAsStr += figure == null? "  " : figure.abbreviation();
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
