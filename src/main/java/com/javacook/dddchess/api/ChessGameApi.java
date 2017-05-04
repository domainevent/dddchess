package com.javacook.dddchess.api;

import com.javacook.dddchess.domain.FigureValueObject;
import com.javacook.dddchess.domain.MoveValueObject;
import com.javacook.dddchess.domain.PositionValueObject;
import scala.concurrent.Future;

import java.util.Optional;


/**
 * Created by javacook on 21.04.17.
 */
public interface ChessGameApi {

    Future<Object> performMove(MoveValueObject move);

    Optional<MoveValueObject> getMove(int index);

    Optional<FigureValueObject> figureAt(PositionValueObject position);

}
