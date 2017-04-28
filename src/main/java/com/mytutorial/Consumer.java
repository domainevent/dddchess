package com.mytutorial;

import akka.actor.UntypedActor;

public class Consumer extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof Integer) {
            System.out.println("<<< Receiving & printing " + msg);
        }
    }
}