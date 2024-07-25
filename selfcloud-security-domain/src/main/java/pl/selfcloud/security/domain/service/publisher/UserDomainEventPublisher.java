package pl.selfcloud.security.domain.service.publisher;


import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import java.util.List;
import org.springframework.stereotype.Component;
import pl.selfcloud.security.api.event.UserDomainEvent;
import pl.selfcloud.security.domain.model.User;


@Component
public class UserDomainEventPublisher extends
    AbstractAggregateDomainEventPublisher<User, UserDomainEvent> {

  public UserDomainEventPublisher(DomainEventPublisher eventPublisher) {
    super(eventPublisher, User.class, User::getId);
  }

  public void publish(Long userId, List<UserDomainEvent> events) {
    // Tworzenie tymczasowego obiektu User
    User user = User.builder().id(userId).build();
    user.setId(userId);
    super.publish(user, events);
  }
}