package pl.selfcloud.security.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.selfcloud.security.api.state.UserState;

@Data
@Builder
@AllArgsConstructor
public class CreatedUserDto {

  private String email;
  private UserState userState;
}
