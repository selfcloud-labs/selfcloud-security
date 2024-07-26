package pl.selfcloud.security.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.customer.api.dto.CustomerDto;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RegistrationDto implements Serializable {
  private String password;
  private CustomerDto customerDto;

}
