package com.javacook.dddchess.rest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.javacook.dddchess.api.ChessGameApi;
import com.javacook.dddchess.api.ChessGameApiImpl;
import com.javacook.dddchess.domain.ChessGameAggregate;
import com.javacook.dddchess.domain.MailSenderAggregate;
import com.javacook.dddchess.domain.MovedEvent;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import scala.concurrent.duration.Duration;

import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import java.util.concurrent.TimeUnit;

@ApplicationPath("dddchess")
public class RestApplication extends ResourceConfig {

    private ActorSystem actorSystem;
    private ChessGameApi chessGameApi;


    public RestApplication() {

        actorSystem = ActorSystem.create("ExampleSystem");
        actorSystem.actorOf(ChessGameAggregate.mkProps(), "chessGame");
        final ActorRef mailSender = actorSystem.actorOf(MailSenderAggregate.mkProps(), "mailSender");
        actorSystem.eventStream().subscribe(mailSender, MovedEvent.class);
//        actorSystem.actorOf(ChessGameAggregate.mkProps().withRouter(new RoundRobinPool(5)), "chessGame");
        chessGameApi = new ChessGameApiImpl(actorSystem);

        register(new AbstractBinder() {
            protected void configure() {
                bind(actorSystem).to(ActorSystem.class);
                bind(chessGameApi).to(ChessGameApi.class);
            }
        });

        register(new JacksonJsonProvider().configure(SerializationFeature.INDENT_OUTPUT, true));

        packages("com.javacook.dddchess.rest");
    }


    @PreDestroy
    private void shutdown() {
        actorSystem.shutdown();
        actorSystem.awaitTermination(Duration.create(15, TimeUnit.SECONDS));
    }

}