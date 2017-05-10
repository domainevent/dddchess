package com.javacook.dddchess.rest;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskTimeoutException;
import com.javacook.dddchess.api.ChessGameApi;
import com.javacook.dddchess.domain.*;
import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;
import com.webcohesion.enunciate.metadata.rs.ResponseCode;
import com.webcohesion.enunciate.metadata.rs.StatusCodes;
import org.glassfish.jersey.server.ManagedAsync;
import scala.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


/**
 * This Service provides a set of functions all around chess game. Moves of figures
 * can be performed as well as queries of figures on the board or previous moves.
 */
@Path("/")
public class RestService {

    @Context
    ActorSystem actorSystem;

    @Context
    ChessGameApi chessGameApi;

    @Context
    UriInfo uriInfo;

    LoggingAdapter log;


    @PostConstruct
    void initialize() {
        log = Logging.getLogger(actorSystem, this);
    }


    /**
     * Checks whether the application is alive
     * @return the current date and time
     */
    @GET
    @Path("isalive")
    @Produces(MediaType.TEXT_PLAIN)
    public String isAlive() {
        log.info("dddchess is alive");
        return "DDD-Chess is alive: " + new Date();
    }


    /**
     * @param color the
     */
    @POST
    @Path("games")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public GameIdValueObject newGame(@FormParam("color") ColorEnum color) {
        log.info("New game, player color {}", color);
        return chessGameApi.newGame();
    }


    /**
     * Returns the figure at the position (<code>horCoord</code>, <code>vertCord</code>)
     * @param horCoord the horizontal coordinate of field on the chess board
     * @param vertCoord the vertical coordinate of field on the chess board
     * @return the chess figure at the given coordinates
     */
    @GET
    @Path("games/{gameId}/board/figure")
    @Produces(MediaType.APPLICATION_JSON)
    @StatusCodes({
            @ResponseCode( code = 200, condition = "ok"),
            @ResponseCode( code = 404, condition = "The field at the given coordinates is empty"),
            @ResponseCode( code = 500, condition = "An exception occured")
    })
    public FigureValueObject figureAt(
            final @NotNull @PathParam("gameId") String gameId,
            final @NotNull @QueryParam("horCoord") HorCoord horCoord,
            final @NotNull @QueryParam("vertCoord") VertCoord vertCoord) {

        log.info("Get figure at horCoord={}, vertCoord={}", horCoord, vertCoord);

        final PositionValueObject position = new PositionValueObject(horCoord, vertCoord);
        final Optional<FigureValueObject> figure = chessGameApi.figureAt(new GameIdValueObject(gameId), position);

        if (figure.isPresent()) {
            return figure.get();
        }
        else {
            throw new NotFoundException("There is no figure at " + position);
        }
    }



    /**
     * Returns the figure at the position (<code>horCoord</code>, <code>vertCord</code>)
     * @return the chess figure at the given coordinates
     */
    @GET
    @Path("games/{gameId}/board")
    @Produces(MediaType.APPLICATION_JSON)
    @StatusCodes({
            @ResponseCode( code = 200, condition = "ok"),
            @ResponseCode( code = 404, condition = "The field at the given coordinates is empty"),
            @ResponseCode( code = 500, condition = "An exception occured")
    })
    public void getBoard(
            final @NotNull @PathParam("gameId") String gameId,
            final @Suspended AsyncResponse resp,
            final @Context Request request) {

        log.info("Get board with id {} ", gameId);

        // API-Call:
        final Future<Object> future = chessGameApi.getBoard(new GameIdValueObject(gameId));

        future.onComplete(new OnComplete<Object>() {

            public void onComplete(Throwable failure, Object result) {
                if (failure == null) {
                    ChessBoardValueObject chessBoard = (ChessBoardValueObject)result;
                    // log.debug("board: " + System.lineSeparator() + chessBoard);

                    CacheControl cc = new CacheControl();
                    cc.setMaxAge(-1);
                    EntityTag etag = new EntityTag(""+ chessBoard.hashCode());
                    ResponseBuilder builder = request.evaluatePreconditions(etag);

                    if (builder == null) {
                        // nothing cached found, so transfer board again
                        builder = Response.ok(chessBoard);
                        builder.tag(etag);
                    }
                    // elsewhere a status vode 304 (NOT MODIFIED) ist produced
                    builder.cacheControl(cc);
                    resp.resume(builder.build());
                }
                else {
                    log.error(failure, failure.getMessage());
                    HashMap<String, Object> json = new HashMap<>();
                    if (failure instanceof AskTimeoutException) {
                        json.put(ErrorCode.ERROR_CODE_KEY, ErrorCode.TIMEOUT);
                        resp.resume(Response.status(503).entity(json).build());
                    }
                    else {
                        resp.resume(Response.serverError().entity(failure).build());
                    }
                }
            }
        }, actorSystem.dispatcher());
    }// getBoard


    /**
     * Returns the nth move of the chess game given by the parameter <code>index</code>
     * @param index the
     * @return
     */
    @GET
    @Path("games/{gameId}/moves/{index}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public MoveValueObject getMove(
            final @NotNull @PathParam("gameId") String gameId,
            final @NotNull @PathParam("index") int index) {

        log.info("Get the {}. move", index);
        final Optional<MoveValueObject> move = chessGameApi.getMove(new GameIdValueObject(gameId), index);
        if (move.isPresent()) {
            return move.get();
        }
        else {
            throw new NotFoundException("There is no move present for index " + index);
        }
    }


    /**
     * Delegates to the same service accepting a JSON object. Here, the move is
     * given by a String representation like "b1-c3" and
     * @param move
     * @param resp
     */
    @POST
    @Path("games/{gameId}/moves")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void performMove(
            final @NotNull @PathParam("gameId") String gameId,
            final @NotNull @FormParam("move") String move,
            final @Suspended AsyncResponse resp) {

        if (move == null) {
            throw new BadRequestException("Missing form parameter 'move'");
        }
        performMove(gameId, new MoveValueObject(move), resp);
    }


    /**
     * Moves a figure according to the values in <code>move</code> - which consists of
     * a "from"- and "to"-part of chess board coordinates.
     * @param move
     * @param resp
     */
    @POST
    @Path("games/{gameId}/moves")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void performMove(
            final @NotNull @PathParam("gameId") String gameId,
            final @NotNull MoveValueObject move,
            final @Suspended AsyncResponse resp) {

        log.info("Try to perform the performMove {}", move);

        // API-Call:
        final Future<Object> future = chessGameApi.performMove(new GameIdValueObject(gameId), move);

        future.onComplete(new OnComplete<Object>() {

            public void onComplete(Throwable failure, Object result) {
                if (failure == null) {
                    log.info("Move index: " + result);
                    HashMap<String, Object> json = new HashMap<>();
                    json.put("index", result);
                    UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                    URI location = ub.path(result.toString()).build();
                    resp.resume(Response.created(location).entity(json).build());
                }
                else {
                    log.error(failure.toString());
                    HashMap<String, Object> json = new HashMap<>();
                    if (failure instanceof AskTimeoutException) {
                        json.put(ErrorCode.ERROR_CODE_KEY, ErrorCode.TIMEOUT);
                        resp.resume(Response.status(503).entity(json).build());
                    }
                    else if (failure instanceof MoveException) {
                        json.put(ErrorCode.ERROR_CODE_KEY, ErrorCode.INVALID_MOVE);
                        json.put(ErrorCode.INVALID_MOVE.name(), failure.getMessage());
                        resp.resume(Response.status(422).entity(json).build());
                    }
                    else {
                        resp.resume(Response.serverError().entity(failure).build());
                    }
                }
            }
        }, actorSystem.dispatcher());
    }// performMove

}
