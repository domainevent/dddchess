package com.javacook.dddchess.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.javacook.dddchess.domain.MoveValueObject;
import com.javacook.dddchess.domain.MoveCommand;
import scala.concurrent.duration.Duration;

import scala.concurrent.Future;

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

}
