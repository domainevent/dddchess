package com.javacook.dddchess.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.javacook.dddchess.domain.FigureValueObject;
import com.javacook.dddchess.domain.MoveValueObject;
import com.javacook.dddchess.domain.MoveCommand;
import com.javacook.dddchess.domain.PositionValueObject;
import scala.concurrent.duration.Duration;

import scala.concurrent.Future;

import static com.javacook.dddchess.domain.FigureValueObject.ColorEnum.BLACK;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.KING;


/**
 * Created by vollmer on 21.04.17.
 */
public class ChessGameApiImpl implements ChessGameApi {

    final ActorSystem actorSystem;

    public ChessGameApiImpl(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public Future<Object> move(MoveValueObject move) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        return Patterns.ask(chessGameActor, new MoveCommand(move), timeout);
    }


    @Override
    public MoveValueObject getMove(int index) {
        return null;
    }


    @Override
    public FigureValueObject figureAt(PositionValueObject position) {
        return new FigureValueObject(KING, BLACK);
    }

}
