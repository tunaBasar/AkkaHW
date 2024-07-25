import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class actorA extends AbstractActor {

    static public Props props(ActorRef actorB) {
        return Props.create(actorA.class, () -> new actorA(actorB));
    }

    private final ActorRef actorB;

    private actorA(ActorRef actorB) {
        this.actorB = actorB;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("start", p -> {
                    actorB.tell("Hi_from_ActorA", getSelf());
                })
                .match(String.class, message -> {
                    System.out.println("ActorA received: " + message);
                })
                .build();
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ActorSystemA", ConfigFactory.load());
        ActorRef actor2 = system.actorSelection("akka://ActorSystemB@127.0.0.1:2552/user/actorB").resolveOne(java.time.Duration.ofSeconds(3)).toCompletableFuture().join();
        ActorRef actor1 = system.actorOf(actorA.props(actor2), "actorA");

        actor1.tell("start", ActorRef.noSender());
    }
}
