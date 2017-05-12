package com.javacook.dddchess.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.javacook.dddchess.domain.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * Created by javacook on 21.04.17.
 */
public class ChessGameApiImpl implements ChessGameApi {

    final ActorSystem actorSystem;

    public ChessGameApiImpl(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }


    @Override
    public GameIdValueObject newGame(Optional<String> note) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Future<Object> future = Patterns.ask(chessGameActor, new NewGameCommand(), timeout);
        try {
            final Object result = Await.result(future, Duration.create(2, TimeUnit.SECONDS));
            return (GameIdValueObject)result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<Object> performMove(GameIdValueObject gameId, MoveValueObject move) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        return Patterns.ask(chessGameActor, new MoveCommand(move), timeout);
    }


    @Override
    public Optional<MoveValueObject> getMove(GameIdValueObject gameId, int index) {
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
    public Future<Object> getBoard(GameIdValueObject gameId) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        return Patterns.ask(chessGameActor, new GetBoardCommand(), timeout);
    }


    @Override
    public Optional<FigureValueObject> figureAt(GameIdValueObject gameId, PositionValueObject position) {
        ActorSelection chessGameActor = actorSystem.actorSelection("/user/chessGame");
        final FiniteDuration duration = Duration.create(2, TimeUnit.SECONDS);
        Timeout timeout = new Timeout(duration);
        final Future<Object> future = Patterns.ask(chessGameActor, new GetFigureCommand(position), timeout);
        try {
            final Object result = Await.result(future, duration);
            return (Optional<FigureValueObject>)result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
