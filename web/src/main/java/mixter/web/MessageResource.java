package mixter.web;

import mixter.EventPublisher;
import mixter.domain.core.message.*;
import mixter.domain.identity.SessionId;
import mixter.domain.identity.SessionProjection;
import mixter.domain.identity.SessionProjectionRepository;
import mixter.domain.identity.UserId;
import net.codestory.http.Request;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Post;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.constants.Headers;
import net.codestory.http.payload.Payload;

import java.util.List;
import java.util.Optional;

import static net.codestory.http.constants.HttpStatus.UNAUTHORIZED;

@Prefix("/api/:userId/messages")
public class MessageResource {
    private TimelineRepository timelineRepository;
    private SessionProjectionRepository sessionRepository;
    private EventPublisher eventPublisher;

    public MessageResource(TimelineRepository timelineRepository, SessionProjectionRepository sessionRepository, EventPublisher eventPublisher) {
        this.timelineRepository = timelineRepository;
        this.sessionRepository = sessionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Get("/:id")
    public TimelineMessage find(String userId,String id) {
        return timelineRepository.getByMessageId(new MessageId(id));
    }

    @Get
    public List<TimelineMessage> list(String userId){
        return timelineRepository.getByUserId(new UserId(userId));
    }

    @Post
    public Payload publish(String ownerId,String content, Request request){
        String sessionId = request.header("X-App-Session");
        Optional<SessionProjection> maybeSession = sessionRepository.getById(new SessionId(sessionId));
        return maybeSession.filter(s->s.hasMail(ownerId)).map(session -> {
            UserId userId = session.getUserId();
            MessageId messageId=Message.publish(new PublishMessage(content, userId), eventPublisher);
           return Payload.created("/api/"+userId.getEmail()+"/messages/"+messageId.getId());
        }).orElse(
            new Payload(UNAUTHORIZED).withHeader(Headers.LOCATION, "/api/sessions")
        );
    }
}
