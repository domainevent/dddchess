package com.javacook.dddchess.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.javacook.dddchess.domain.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.javacook.dddchess.domain.FigureValueObject.ColorEnum.BLACK;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.KING;


/**
 * Created by javacook on 21.04.17.
 */
public class ChessGameApiImpl implements ChessGameApi {

    final ActorSystem actorSystem;

    public ChessGameApiImpl(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }


    @Override
    public void newGame() {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Patterns.ask(chessGameActor, new NewGameCommand(), timeout);
    }

    @Override
    public Future<Object> performMove(MoveValueObject move) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        return Patterns.ask(chessGameActor, new MoveCommand(move), timeout);
    }


    @Override
    public Optional<MoveValueObject> getMove(int index) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Future<Object> future = Patterns.ask(chessGameActor, new GetMoveCommand(index), timeout);
        try {
            final Object result = Await.result(future, Duration.create(2, TimeUnit.SECONDS));
            return (Optional<MoveValueObject>)result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<FigureValueObject> figureAt(PositionValueObject position) {
        return Optional.of(new FigureValueObject(KING, BLACK));
    }

}
