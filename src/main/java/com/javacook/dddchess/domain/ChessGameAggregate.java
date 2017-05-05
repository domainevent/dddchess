package com.javacook.dddchess.domain;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.javacook.dddchess.domain.FigureValueObject.ColorEnum;

import java.util.Optional;

import static com.javacook.dddchess.domain.FigureValueObject.ColorEnum.WHITE;


public class ChessGameAggregate extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    GameIdValueObject gameId = new GameIdValueObject();

    MoveSequenceEntity moveSequence = new MoveSequenceEntity(gameId);
    ChessBoardEntity chessBoard = new ChessBoardEntity(gameId);
    Optional<ColorEnum> lastMoveColor;


    /**
     * Constructor
     */
    public ChessGameAggregate() {
        initializeCommandHandler();
        newGame();
    }

    private void initializeCommandHandler() {
        receive(ReceiveBuilder.
                match(MoveCommand.class, moveCommand -> {
                    log.info("received MoveCommand: {}", moveCommand);
                    // TimeUnit.SECONDS.sleep(3);
                    try {
                        final Integer result = this.performMove(moveCommand.move);
                        sender().tell(result, self());
                    }
                    catch (MoveException e) {
                        final Status.Failure failure = new Status.Failure(e);
                        sender().tell(failure, self());
                    }

                })
                .match(GetMoveCommand.class, getMoveCommand -> {
                    final Optional<MoveValueObject> move = this.getMove(getMoveCommand.moveIndex);
                    sender().tell(move, self());
                })
                .match(NewGameCommand.class, newGameCommand -> {
                    this.newGame();
                })
                .matchAny(o -> log.warning("Received unknown message!")).build()
        );
    }



    // Business
    //

    public Optional<MoveValueObject> getMove(int moveIndex) {
        return moveSequence.getMove(moveIndex);
    }

    public int performMove(MoveValueObject move) throws MoveException {
        // begin transaction
        moveSequence.addMove(move);
        chessBoard.performMove(move, lastMoveColor);
        lastMoveColor = (lastMoveColor.isPresent())?
                Optional.of(lastMoveColor.get().swap()) : Optional.of(ColorEnum.WHITE);

        this.getContext().system().eventStream().publish(new MovedEvent(move));
        return moveSequence.noMoves()-1;
        // end transaction
    }

    public void newGame() {
        // begin transaction
        moveSequence.initialize();
        chessBoard.initialize();
        lastMoveColor = Optional.empty();
        // end transaction
    }



    public static Props mkProps() {
        return Props.create(ChessGameAggregate.class);
    }

    @Override
    public void preStart() {
        log.debug("Starting Chess Game");
    }



}