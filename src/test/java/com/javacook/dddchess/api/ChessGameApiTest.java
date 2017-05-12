package com.javacook.dddchess.api;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.javacook.dddchess.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.nio.file.attribute.FileStoreAttributeView;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.javacook.dddchess.domain.ColorEnum.BLACK;
import static com.javacook.dddchess.domain.ColorEnum.WHITE;
import static com.javacook.dddchess.domain.FigureValueObject.FigureEnum.ROOK;


/**
 * Created by vollmer on 10.05.17.
 */
public class ChessGameApiTest {

    private static ActorSystem actorSystem = null;
    private ChessGameApi api;

    @BeforeClass
    public static void beforeClass() {
        actorSystem = ActorSystem.create("ExampleSystem");
        actorSystem.actorOf(ChessGameAggregate.mkProps(), "chessGame");
    }

    @Before
    public void setUp() {
        api = new ChessGameApiImpl(actorSystem);
    }

    @Test
    public void newGame() throws Exception {
        final GameIdValueObject gameId = api.newGame(Optional.empty());
        Assert.assertNotNull(gameId);
        Assert.assertNotNull(gameId.id);
    }


    @Test
    public void performMove() throws Exception {

        PositionValueObject posFrom1 = new PositionValueObject("e2");
        PositionValueObject posTo1 = new PositionValueObject("e4");

        final ChessBoardValueObject expected =
                new ChessBoardValueObject(MockDataFactory.createInitialChessBoard()) {{
                    final FigureValueObject figure1 = getFigureAtPosition(posFrom1);
                    setFigureToPosition(posFrom1, null);
                    setFigureToPosition(posTo1, figure1);
                }};

        final GameIdValueObject gameId = api.newGame(Optional.empty());
        api.performMove(gameId, new MoveValueObject(posFrom1, posTo1));
        final Future<Object> future = api.getBoard(gameId);

        final ChessBoardValueObject actual =
                (ChessBoardValueObject) Await.result(future, Duration.create(2, TimeUnit.SECONDS));

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void getMoveWithoutPreviousMove() throws Exception {
        final GameIdValueObject gameId = api.newGame(Optional.of("My note"));
        final Optional<MoveValueObject> move = api.getMove(gameId, 0);
        Assert.assertFalse(move.isPresent());
    }


    @Test
    public void getMoveWithPreviousMove() throws Exception {
        final GameIdValueObject gameId = api.newGame(Optional.of("My note"));
        final MoveValueObject moveToPerform = new MoveValueObject("b1-c3");
        api.performMove(gameId, moveToPerform);
        final Optional<MoveValueObject> movePerformed = api.getMove(gameId, 0);
        Assert.assertTrue(movePerformed.isPresent());
        final MoveValueObject actual = movePerformed.get();
        Assert.assertEquals(moveToPerform, actual);
    }


    @Test
    public void getBoard() throws Exception {
        final ChessBoardValueObject expected = MockDataFactory.createInitialChessBoard();

        final GameIdValueObject gameId = api.newGame(Optional.of("My note"));
        final Future<Object> future = api.getBoard(gameId);

        final ChessBoardValueObject actual =
                (ChessBoardValueObject) Await.result(future, Duration.create(2, TimeUnit.SECONDS));
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void figureAt() throws Exception {
        final GameIdValueObject gameId = api.newGame(Optional.of("My note"));
        final Optional<FigureValueObject> figure1 = api.figureAt(gameId, new PositionValueObject("a1"));
        Assert.assertTrue(figure1.isPresent());
        final FigureValueObject expected1 = new FigureValueObject(ROOK, WHITE);
        Assert.assertEquals(expected1, figure1.get());

        final Optional<FigureValueObject> figure2 = api.figureAt(gameId, new PositionValueObject("h8"));
        Assert.assertTrue(figure2.isPresent());
        final FigureValueObject expected2 = new FigureValueObject(ROOK, BLACK);
        Assert.assertEquals(expected2, figure2.get());

        final Optional<FigureValueObject> figure3 = api.figureAt(gameId, new PositionValueObject("e5"));
        Assert.assertFalse(figure3.isPresent());
    }

}