package pl.selfcloud.security.saga.user.create.publisher;


import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import java.util.List;
import org.springframework.stereotype.Component;
import pl.selfcloud.security.api.event.UserDomainEvent;
import pl.selfcloud.security.saga.user.create.CreateUserSagaState;


@Component
public class UserDomainSagaPublisher extends
    AbstractAggregateDomainEventPublisher<CreateUserSagaState, UserDomainEvent> {

  public UserDomainSagaPublisher(DomainEventPublisher eventPublisher) {
    super(eventPublisher, CreateUserSagaState.class, CreateUserSagaState::getUserId);
  }

  public void publish(CreateUserSagaState state, List<UserDomainEvent> events) {
    // Tworzenie tymczasowego obiektu User

    super.publish(state, events);
  }
}