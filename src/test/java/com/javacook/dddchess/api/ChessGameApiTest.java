package com.javacook.dddchess.api;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.javacook.dddchess.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.nio.file.attribute.FileStoreAttributeView;
import java.util.concurrent.TimeUnit;


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
        final GameIdValueObject gameId = api.newGame();
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

        final GameIdValueObject spielId = api.newGame();
        api.performMove(spielId, new MoveValueObject(posFrom1, posTo1));
        final Future<Object> future = api.getBoard(spielId);

        final ChessBoardValueObject actual =
                (ChessBoardValueObject) Await.result(future, Duration.create(2, TimeUnit.SECONDS));

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void getMove() throws Exception {
    }


    @Test
    public void getBoard() throws Exception {
        final ChessBoardValueObject expected = MockDataFactory.createInitialChessBoard();

        final GameIdValueObject spielId = api.newGame();
        final Future<Object> future = api.getBoard(spielId);

        final ChessBoardValueObject actual =
                (ChessBoardValueObject) Await.result(future, Duration.create(2, TimeUnit.SECONDS));
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void figureAt() throws Exception {
    }

}