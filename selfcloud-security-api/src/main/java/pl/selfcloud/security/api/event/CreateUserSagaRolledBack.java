package pl.selfcloud.security.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserSagaRolledBack implements UserDomainEvent {

  private long userId;
  private String message;

}