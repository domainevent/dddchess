package com.javacook.dddchess.api;

import com.javacook.dddchess.domain.MoveValueObject;
import scala.concurrent.Future;

/**
 * Created by vollmer on 21.04.17.
 */
public interface ChessGameApi {

    Future<Object> move(MoveValueObject move);

}
