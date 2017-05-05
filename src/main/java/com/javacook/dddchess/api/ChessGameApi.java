package com.javacook.dddchess.api;

import com.javacook.dddchess.domain.*;
import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;
import scala.concurrent.Future;

import java.util.Optional;


/**
 * Created by javacook on 21.04.17.
 */
public interface ChessGameApi {

    GameIdValueObject newGame();

    Future<Object> performMove(MoveValueObject move);

    Optional<MoveValueObject> getMove(int index);

    Future<Object> getBoard();

    Optional<FigureValueObject> figureAt(PositionValueObject position);

}
