package com.javacook.dddchess.rest;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.javacook.dddchess.api.ChessGameApi;
import com.javacook.dddchess.domain.FigureValueObject;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;


@Path("/chessgame")
public class RestService {

    @Context
    ActorSystem actorSystem;
    @Context
    ChessGameApi chessGameApi;
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


    @POST
    @Path("move")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void postMove(@FormParam("move") String move,
                         @Suspended final AsyncResponse resp) {

        postMove(new MoveValueObject(move), resp);
    }


    @POST
    @Path("move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void postMove(MoveValueObject move,
                         @Suspended final AsyncResponse resp) {

        log.info("Try to perform the move {}", move);

        // API-Call:
        final Future<Object> future = chessGameApi.move(move);

        future.onComplete(new OnComplete<Object>() {

            public void onComplete(Throwable failure, Object result) {
                if (failure == null) {
                    log.info("Move result: " + result);
                    HashMap<String, Object> response = new HashMap<>();
                    response.put("results", result);
                    resp.resume(Response.ok().entity(response).build());
                }
                else {
                    log.error(failure, failure.getMessage());
                    HashMap<String, String> response = new HashMap<>();
                    response.put("error", failure.getMessage());
                    resp.resume(Response.serverError().entity(response).build());
                }
            }
        }, actorSystem.dispatcher());
    }// move

}
