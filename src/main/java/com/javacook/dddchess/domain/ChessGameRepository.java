package com.javacook.dddchess.domain;

/**
 * Created by javacook on 30.04.17.
 */
public class ChessGameRepository {

    private static ChessBoardValueObject chessBoard = new ChessBoardValueObject();

    ChessBoardValueObject findChessBoard() {
        return chessBoard;
    }

    void saveChessBoard(ChessBoardValueObject chessBoard) {
        ChessGameRepository.chessBoard = chessBoard;
    }

}
