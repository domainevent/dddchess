package de.jrthies.fuchsundhasen;

import akka.actor.*;
import java.util.*;
import java.util.concurrent.*;
import scala.concurrent.duration.FiniteDuration;

/**
 * Hier wird ein Spielfeld simuliert, auf dem sich ein Fuchs und mehrere Hasen bewegen. Der Fuchs frisst jeden Hasen, der sich auf der
 * gleichen Position wie er selbst befindet. Treffen Hasen aufeinander, vermehren sie sich. Die Simulation endet, wenn keine Hasen mehr
 * übrig sind.
 */
public class Simulation extends UntypedActor {

    public static final int FELDBREITE = 5;
    public static final String TRIGGER = "";
    private final FiniteDuration FUCHS_FREQ = new FiniteDuration(100, TimeUnit.MILLISECONDS);
    private final FiniteDuration HASE_FREQ = new FiniteDuration(200, TimeUnit.MILLISECONDS);
    private final FiniteDuration DELAY = new FiniteDuration(10, TimeUnit.MILLISECONDS);
    private final int MAX_ANZAHL_HASEN = 6;

    private final ActorSystem actorSystem = getContext().system();
    private final Random r = new Random();

    private final Map<ActorRef, Position> hasenPositionen = new ConcurrentHashMap<>();
    private Position fuchsPosition;

    /**
     * Beim Programmstart wird das ActorSystem mit der Simulation als Actor gestartet.
     *
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem.create().actorOf(Props.create(Simulation.class));
    }

    /**
     * Im Konstruktor werden die beteiligten Tiere der Simulation erzeugt.
     */
    public Simulation() {
        //Der Fuchs beginnt in einer der Ecken des Spielfelds, die drei Hasen in den drei anderen Ecken.
        createFox(new Position(FELDBREITE, FELDBREITE));
        createRabbit(new Position(0, 0));
        createRabbit(new Position(FELDBREITE, 0));
        createRabbit(new Position(0, FELDBREITE));
    }

    /**
     * Die Simulation ist selbst ein Actor und empfängt permanent die neuen Positionsangaben aller Tiere. Die Hasen-Positionen werden
     * getrennt von der Fuchs-Position gespeichert.
     *
     * @param message Positions-Objekt
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Position) {
            Position neuePosition = (Position) message;
            if (getSender().path().name().equals("Fuchs")) {
                fuchsPosition = neuePosition;
            } else {
                hasenPositionen.put(getSender(), neuePosition);
            }
            printPositions();
            checkCollisions();
        }
    }

    /**
     * Der Fuchs-Actor erhält einen Namen, und seine Position muss getrennt gespeichert werden. Durch den Namen kann verhindert werden, dass
     * mehrere Füchse gleichzeitig existieren.
     *
     * @param pos Position zum Start
     */
    private void createFox(Position pos) {
        ActorRef fuchs = actorSystem.actorOf(Props.create(Tier.class), "Fuchs");
        fuchs.tell(pos, null);
        fuchsPosition = pos;
        actorSystem.scheduler().schedule(DELAY, FUCHS_FREQ, fuchs, TRIGGER, actorSystem.dispatcher(), getSelf());
        System.out.println(fuchs.path().name() + " auf " + pos);
    }

    /**
     * Hasen-Actors werden ohne Namen erzeugt, es kann beliebig viele davon geben. Die Positionen aller Hasen werden in einer Map
     * gespeichert. Jeder Hase erhält über einen Zeitgeber in regelmäßigen Intervallen Trigger-Nachrichten, die Bewegungen auslösen.
     *
     * @param pos Position zum Start
     */
    private void createRabbit(Position pos) {
        ActorRef hase = actorSystem.actorOf(Props.create(Tier.class));
        hase.tell(pos, getSelf());
        hasenPositionen.put(hase, pos);
        actorSystem.scheduler().schedule(DELAY, HASE_FREQ, hase, TRIGGER, actorSystem.dispatcher(), getSelf());
        System.out.println("Neuer Hase " + hase.path().name() + " auf " + pos);
    }

    /**
     * Ausgabe der Positionen von Fuchs und allen Hasen.
     */
    private void printPositions() {
        StringBuilder poslist = new StringBuilder();
        for (Position position : hasenPositionen.values()) {
            poslist.append(position.toString()).append("  ");
        }
        System.out.println("Fuchs auf Position: " + fuchsPosition + ", Hasen auf Positionen: " + poslist.toString());
    }

    /**
     * Positionsvergleich zwischen Fuchs und sämtlichen Hasen. Das Programm endet, wenn keine Hasen mehr übrig sind.
     */
    private void checkCollisions() {
        Set<ActorRef> actorRefs = hasenPositionen.keySet();
        for (ActorRef rabbitRef : actorRefs) {
            if (hasenPositionen.get(rabbitRef).equals(fuchsPosition)) {
                System.out.println("Hase gefressen!");
                rabbitRef.tell(PoisonPill.getInstance(), getSelf());
                hasenPositionen.remove(rabbitRef);
            }
        }
        if (hasenPositionen.isEmpty()) {
            System.out.println("Keine mehr da!");
            actorSystem.shutdown();
        } else {
            //Wenn die Anzahl der unterschiedlichen Hasen-Positionen kleiner ist als die Anzahl der Hasen,
            //hat eine Hasen-Kollision stattgefunden.
            Set<Position> verschiedenePositionen = new HashSet<Position>();
            verschiedenePositionen.addAll(hasenPositionen.values());
            int anzahlHasen = hasenPositionen.size();
            if (verschiedenePositionen.size() < anzahlHasen && anzahlHasen < MAX_ANZAHL_HASEN) {
                //Treffen sich Hasen, vermehren sie sich.
                createRabbit(new Position(r.nextInt(FELDBREITE), r.nextInt(FELDBREITE)));
            }
        }
    }

}