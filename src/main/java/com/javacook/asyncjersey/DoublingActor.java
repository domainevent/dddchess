package com.javacook.asyncjersey;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

import java.util.concurrent.TimeUnit;

public class DoublingActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static Props mkProps() {
        return Props.create(DoublingActor.class);
    }

    @Override
    public void preStart() {
        log.debug("starting");
    }


    public DoublingActor() {
        receive(ReceiveBuilder.
                match(Integer.class, message -> {
                    log.info("received message: " + (Integer)message);
//                    TimeUnit.SECONDS.sleep(3);
                    sender().tell(message*2, self());
                }).
                matchAny(o -> log.warning("received unknown message")).build()
        );
    }

//    @Override
//    public void onReceive(Object message) throws Exception {
//        if (message instanceof Integer) {
//            log.debug("received message: " + (Integer)message);
//            getSender().tell((Integer)message*2, getSelf());
//        } else {
//            unhandled(message);
//        }
//    }

}