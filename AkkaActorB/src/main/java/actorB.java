import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class actorB extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    System.out.println("ActorB received: " + message);
                    if (message.equals("Hi_from_ActorA")) {
                        getSender().tell("Hi_from_ActorB", getSelf());
                    }
                })
                .build();
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ActorSystemB", ConfigFactory.load());
        system.actorOf(Props.create(actorB.class), "actorB");
    }
}
