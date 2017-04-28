package de.jrthies.fuchsundhasen;

import akka.actor.UntypedActor;
import java.util.Random;

public class Tier extends UntypedActor {

    private Position position;
    private final Random r = new Random();

    /**
     * Man kann dem Tier eine Startposition zuweisen oder irgend ein anderes Objekt senden, damit das Tier sich bewegt und dem Absender
     * seine neue Position mitteilt.
     *
     * @param msg Positions-Objekt oder etwas anderes
     * @throws Exception
     */
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Position) {
            this.position = (Position) msg;
        } else {
            move();
            getSender().tell(position, getSelf());
        }
    }

    /**
     * Zufällige Bewegung auf dem Spielfeld.
     */
    private void move() {
        switch (r.nextInt(4)) {
            case 0:
                this.position = new Position(
                        position.getX(),
                        position.getY() > 0 ? position.getY() - 1 : 0);
                break;
            case 1:
                this.position = new Position(
                        position.getX() < Simulation.FELDBREITE ? position.getX() + 1 : Simulation.FELDBREITE,
                    position.getY());
                break;
            case 2:
                this.position = new Position(
                        position.getX(),
                        position.getY() < Simulation.FELDBREITE ? position.getY() + 1 : Simulation.FELDBREITE);
                break;
            case 3:
                this.position = new Position(
                        position.getX() > 0 ? position.getX() - 1 : 0,
                        position.getY());
                break;
            default:
                break;
        }
    }

}