package mixter.infra;

import mixter.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

public class SynchronousEventPublisherTest {
    private SpyEvenHandler spyEvenHandler;

    @Before
    public void setUp() throws Exception {
        spyEvenHandler = new SpyEvenHandler();
    }

    @Test
    public void GivenHandlerWhenPublishThenCallHandler() {
        Consumer<EventA> handler = spyEvenHandler::apply;
        SynchronousEventPublisher publisher=new SynchronousEventPublisher();
        publisher.register(EventA.class, handler);
        EventA event = new EventA(new AnAggregateId());

        publisher.publish(event);

        assertThat(spyEvenHandler.isCalled()).isTrue();
    }

    @Test
    public void GivenDifferentHandlersWhenPublishThenCallRightHandler() {
        Consumer<EventA> handlerOfA = spyEvenHandler::apply;
        Consumer<EventB> handlerOfB = new SpyEvenHandler()::apply;
        SynchronousEventPublisher publisher=new SynchronousEventPublisher();
        publisher.register(EventA.class, handlerOfA);
        publisher.register(EventB.class, handlerOfB);
        EventA event = new EventA(new AnAggregateId());

        publisher.publish(event);

        assertThat(spyEvenHandler.isCalled()).isTrue();
    }
    class SpyEvenHandler {
        boolean called=false;
        private List<Event> events=new ArrayList<>();

        public void apply(EventA event){
            this.events.add(event);
            called=true;
        }
        public void apply(EventB event){
            this.events.add(event);
            called=true;
        }

        public boolean isCalled() {
            return called;
        }
    }
}
