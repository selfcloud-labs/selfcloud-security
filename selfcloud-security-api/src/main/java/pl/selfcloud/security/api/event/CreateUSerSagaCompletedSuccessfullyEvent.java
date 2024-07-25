package pl.selfcloud.security.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateUSerSagaCompletedSuccessfullyEvent implements UserDomainEvent {

  private long userId;
  private String message;

}