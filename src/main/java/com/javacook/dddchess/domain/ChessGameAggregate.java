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
                .match(GetFigureCommand.class, getBoardCommand -> {
                    final Optional<FigureValueObject> figure = this.getFigureAtPosition(getBoardCommand.position);
                    sender().tell(figure, self());
                })
                .match(GetBoardCommand.class, getBoardCommand -> {
                    final ChessBoardValueObject board = this.getBoard();
                    sender().tell(board, self());
                })
                .match(NewGameCommand.class, newGameCommand -> {
                    final GameIdValueObject gameId = this.newGame();
                    sender().tell(gameId, self());
                })
                .matchAny(command -> {
                    log.warning("Received unknown message: " + command);
                    final UnknownCommandException exception = new UnknownCommandException(command);
                    final Status.Failure failure = new Status.Failure(exception);
                    sender().tell(failure, self());
                }).build()
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

    public GameIdValueObject newGame() {
        // begin transaction
        moveSequence.initialize();
        chessBoard.initialize();
        lastMoveColor = Optional.empty();
        // end transaction
        return gameId;
    }

    public ChessBoardValueObject getBoard() {
        return new ChessBoardValueObject(chessBoard.board);
    }


    public Optional<FigureValueObject> getFigureAtPosition(PositionValueObject position) {
        return chessBoard.getFigureAtPosition(position);
    }


    public static Props mkProps() {
        return Props.create(ChessGameAggregate.class);
    }

    @Override
    public void preStart() {
        log.debug("Starting Chess Game");
    }

}