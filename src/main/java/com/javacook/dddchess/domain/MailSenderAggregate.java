package com.javacook.dddchess.domain;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class MailSenderAggregate extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static Props mkProps() {
        return Props.create(MailSenderAggregate.class);
    }

    @Override
    public void preStart() {
        log.debug("Starting Mail Sender");
    }


    public MailSenderAggregate() {
        initializeEventHandler();
    }


    private void initializeEventHandler() {
        receive(ReceiveBuilder.
                match(MovedEvent.class, movedEvent -> {
                    log.info("Mailer received MovedEvent: " + movedEvent);
                }).
                matchAny(o -> log.warning("Received unknown message!")).build()
        );
    }

}