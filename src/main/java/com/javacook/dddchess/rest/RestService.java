package com.javacook.dddchess.rest;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.javacook.dddchess.api.ChessGameApi;
import com.javacook.dddchess.domain.FigureValueObject;
import com.javacook.dddchess.domain.MoveException;
import com.javacook.dddchess.domain.MoveValueObject;
import com.javacook.dddchess.domain.PositionValueObject;
import com.javacook.dddchess.domain.PositionValueObject.HorCoord;
import com.javacook.dddchess.domain.PositionValueObject.VertCoord;
import org.glassfish.jersey.server.ManagedAsync;
import scala.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


@Path("/chessgame")
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


    @GET
    @Path("isalive")
    @Produces(MediaType.TEXT_PLAIN)
    public String figureAt() {
        log.info("dddchess is alive");
        return "DDD-Chess is alive: " + new Date();
    }


    @GET
    @Path("board")
    @Produces(MediaType.APPLICATION_JSON)
    public FigureValueObject figureAt(@QueryParam("horCoord") HorCoord horCoord,
                                      @QueryParam("vertCoord") VertCoord vertCoord) {

        log.info("Get figure at horCoord={}, vertCoord={}", horCoord, vertCoord);
        return chessGameApi.figureAt(new PositionValueObject(horCoord, vertCoord));
    }


    @GET
    @Path("move/{index}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public MoveValueObject getMove(@PathParam("index") int index) {
        log.info("Get {}th move", index);
        final Optional<MoveValueObject> move = chessGameApi.getMove(index);
        if (move.isPresent()) {
            return move.get();
        }
        else {
            throw new NotFoundException("There is no move present for index " + index);
        }
    }


    @POST
    @Path("move")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void postMove(@FormParam("move") String move,
                         @Suspended final AsyncResponse resp) {

        if (move == null) {
            throw new BadRequestException("Missing form parameter 'move'");
        }
        postMove(new MoveValueObject(move), resp);
    }


    @POST
    @Path("move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void postMove(MoveValueObject move,
                         @Suspended final AsyncResponse resp) {

        log.info("Try to perform the performMove {}", move);

        // API-Call:
        final Future<Object> future = chessGameApi.performMove(move);

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
                    // FIXME: TIMEOUT-Fall behandeln
                    log.error(failure, failure.getMessage());
                    HashMap<String, Object> json = new HashMap<>();
                    if (failure instanceof MoveException) {
                        json.put("error code", ErrorCode.INVALID_MOVE);
                        json.put(ErrorCode.INVALID_MOVE.name(), failure.getMessage());
                        resp.resume(Response.status(422).entity(json).build());
                    }
                    else {
                        resp.resume(Response.serverError().entity(failure).build());
                    }
                }
            }
        }, actorSystem.dispatcher());
    }// postMove

}
