package com.mytutorial;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.concurrent.TimeUnit;

public class Producer {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("generate-numbers-one-to-ten");
        ActorRef printNumbersConsumer = system.actorOf(Props.create(Consumer.class));

        for (int i = 1; i <= 10; i++) {
            System.out.println(">>> Producing & sending a number " +  i);
            printNumbersConsumer.tell(i, ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
        }

        system.shutdown();
        System.out.println("===== Finished producing & sending numbers 1 to 10");

    }
}
