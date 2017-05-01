package com.javacook.dddchess.domain;

/**
 * Created by javacook on 30.04.17.
 */
public class ChessGameRepository {

    private static ChessBoardEntity chessBoard = new ChessBoardEntity();

    ChessBoardEntity findChessBoard() {
        return chessBoard;
    }

    void saveChessBoard(ChessBoardEntity chessBoard) {
        ChessGameRepository.chessBoard = chessBoard;
    }

}
