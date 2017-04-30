package com.javacook.dddchess.domain;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class ChessGameAggregate extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static Props mkProps() {
        return Props.create(ChessGameAggregate.class);
    }

    @Override
    public void preStart() {
        log.debug("Starting Chess Game");
    }


    /**
     * Constructor
     */
    public ChessGameAggregate() {
        initializeCommandHandler();
    }

    private void initializeCommandHandler() {
        receive(ReceiveBuilder.
                match(MoveCommand.class, moveCommand -> {
                    log.info("received MoveCommand: " + moveCommand);
                    // TimeUnit.SECONDS.sleep(3);
                    final Integer result = this.move(moveCommand.move);
                    sender().tell(result, self());
                }).
                matchAny(o -> log.warning("Received unknown message!")).build()
        );
    }

    final ChessGameRepository chessGameRepository = new ChessGameRepository();

    // Business
    //
    public Integer move(MoveValueObject move) {

        final ChessBoardValueObject chessBoard = chessGameRepository.findChessBoard();

        this.getContext().system().eventStream().publish(new MovedEvent(move));
        return move.hashCode();
    }

}