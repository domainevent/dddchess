package com.javacook.dddchess.api;

import com.javacook.dddchess.domain.*;
import scala.concurrent.Future;

import java.util.Optional;


/**
 * Created by javacook on 21.04.17.
 */
public interface ChessGameApi {

    GameIdValueObject newGame(Optional<String> note);

    Future<Object> performMove(GameIdValueObject gameId, MoveValueObject move);

    Optional<MoveValueObject> getMove(GameIdValueObject gameId, int index);

    Future<Object> getBoard(GameIdValueObject gameId);

    Optional<FigureValueObject> figureAt(GameIdValueObject gameId, PositionValueObject position);

}
