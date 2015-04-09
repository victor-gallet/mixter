package mixter.infra;

import mixter.Event;
import mixter.EventPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SynchronousEventPublisher implements EventPublisher{
    private Map<Class, List<Consumer>> allHandlers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void publish(Event event) {
        List<Consumer> handlers = allHandlers.getOrDefault(event.getClass(), emptyList());
        handlers.forEach(handler -> handler.accept(event));
    }

    private List<Consumer> emptyList() {
        return new ArrayList<>();
    }

    public <T> void register(Class<T> eventClass, Consumer<T> handler) {
        List<Consumer> handlers = allHandlers.getOrDefault(eventClass, emptyList());
        handlers.add(handler);
        allHandlers.put(eventClass, handlers);
    }
}
