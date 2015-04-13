package mixter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import mixter.domain.EventStore;
import mixter.domain.core.message.TimelineRepository;
import mixter.domain.core.message.events.MessageDeleted;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.message.events.MessageRepublished;
import mixter.domain.core.message.handlers.UpdateTimeline;
import mixter.domain.identity.SessionProjectionRepository;
import mixter.domain.identity.events.UserConnected;
import mixter.domain.identity.events.UserDisconnected;
import mixter.domain.identity.handlers.RegisterSession;
import mixter.infra.InMemoryEventStore;
import mixter.infra.PersistedEventPublisher;
import mixter.infra.SynchronousEventPublisher;
import mixter.infra.repositories.EventSessionRepository;
import mixter.infra.repositories.EventUserIdentityRepository;
import mixter.infra.repositories.InMemorySessionProjectionRepository;
import mixter.infra.repositories.InMemoryTimelineRepository;
import net.codestory.http.WebServer;
import net.codestory.http.extensions.Extensions;
import net.codestory.http.misc.Env;

public class Bootstrap {
    public static void main(String[] args) {
        EventStore eventStore = new InMemoryEventStore();
        SynchronousEventPublisher dispatcher = new SynchronousEventPublisher();
        PersistedEventPublisher eventPublisher = new PersistedEventPublisher(eventStore, dispatcher);

        // Identity
        SessionProjectionRepository sessionProjectionRepository = new InMemorySessionProjectionRepository();
        EventSessionRepository sessionRepository = new EventSessionRepository(eventStore);
        EventUserIdentityRepository userIdentityRepository = new EventUserIdentityRepository(eventStore);

        RegisterSession registerSessionHandler = new RegisterSession(sessionProjectionRepository);
        dispatcher.register(UserConnected.class, registerSessionHandler::apply);
        dispatcher.register(UserDisconnected.class, registerSessionHandler::apply);

        IdentityResource identityResource = new IdentityResource(eventPublisher);
        SessionResource sessionResource = new SessionResource(userIdentityRepository, sessionRepository, eventPublisher);

        //Message
        TimelineRepository timelineRepository = new InMemoryTimelineRepository();
        UpdateTimeline updateTimeline = new UpdateTimeline(timelineRepository);
        dispatcher.register(MessagePublished.class, updateTimeline::apply);
        dispatcher.register(MessageRepublished.class, updateTimeline::apply);
        dispatcher.register(MessageDeleted.class, updateTimeline::apply);

        MessageResource messageResource = new MessageResource(timelineRepository, sessionProjectionRepository, eventPublisher);

        new WebServer().configure(routes -> routes
            .setExtensions(new JsonExtension())
            .get("/", "yeah")
            .add(identityResource)
            .add(sessionResource)
            .add(messageResource)
        ).start();
    }
}

class JsonExtension implements Extensions {
    @Override
    public ObjectMapper configureOrReplaceObjectMapper(ObjectMapper defaultObjectMapper, Env env) {
        defaultObjectMapper
                .registerModule(new JSR310Module())
                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return defaultObjectMapper;
    }
}
