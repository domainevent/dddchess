package com.javacook.dddchess.rest;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.javacook.dddchess.api.ChessGameApi;
import com.javacook.dddchess.domain.MoveValueObject;
import com.javacook.dddchess.domain.PositionValueObject;
import org.glassfish.jersey.server.ManagedAsync;
import scala.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/chessgame")
public class RestService {

    @Context ActorSystem actorSystem;
    @Context
    ChessGameApi chessGameApi;
    LoggingAdapter log;

    @PostConstruct
    void initialize() {
        log = Logging.getLogger(actorSystem, this);
    }

    @GET
    @Path("move")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getExamples (
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @Suspended final AsyncResponse resp) {

        log.info("Try to move from " + from + " to " + to);

        // API-Call:
        final MoveValueObject move = new MoveValueObject(new PositionValueObject(from), new PositionValueObject(to));
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

    }
}
