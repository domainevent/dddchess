package com.javacook.dddchess.api;


import com.javacook.dddchess.domain.ChessBoardValueObject;
import com.javacook.dddchess.domain.PositionValueObject;

import static com.javacook.dddchess.domain.ColorEnum.BLACK;
import static com.javacook.dddchess.domain.ColorEnum.WHITE;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.*;
import static com.javacook.dddchess.domain.PositionValueObject.HorCoord.*;
import static com.javacook.dddchess.domain.PositionValueObject.VertCoord.*;


/**
 * Created by vollmer on 09.05.17.
 */
public class MockDataFactory {

    public static ChessBoardValueObject createInitialChessBoard() {
        return new ChessBoardValueObject() {{
            setFigureToPosition(A, _1, ROOK, WHITE);
            setFigureToPosition(B, _1, KNIGHT, WHITE);
            setFigureToPosition(C, _1, BISHOP, WHITE);
            setFigureToPosition(D, _1, QUEEN, WHITE);
            setFigureToPosition(E, _1, KING, WHITE);
            setFigureToPosition(F, _1, BISHOP, WHITE);
            setFigureToPosition(G, _1, KNIGHT, WHITE);
            setFigureToPosition(H, _1, ROOK, WHITE);
            for (PositionValueObject.HorCoord h : PositionValueObject.HorCoord.values()) {
                setFigureToPosition(h, _2, PAWN, WHITE);
                setFigureToPosition(h, _7, PAWN, BLACK);
            }
            setFigureToPosition(A, _8, ROOK, BLACK);
            setFigureToPosition(B, _8, KNIGHT, BLACK);
            setFigureToPosition(C, _8, BISHOP, BLACK);
            setFigureToPosition(D, _8, QUEEN, BLACK);
            setFigureToPosition(E, _8, KING, BLACK);
            setFigureToPosition(F, _8, BISHOP, BLACK);
            setFigureToPosition(G, _8, KNIGHT, BLACK);
            setFigureToPosition(H, _8, ROOK, BLACK);
        }};
    }

}
