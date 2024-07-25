package pl.selfcloud.security.api.saga.command;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.common.model.dto.RegistrationDto;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateCustomerCommand implements Command {

  private long userId;
  private RegistrationDto registrationDto;
}
