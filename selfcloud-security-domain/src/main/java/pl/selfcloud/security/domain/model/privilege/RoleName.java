package pl.selfcloud.security.domain.model.privilege;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum RoleName implements GrantedAuthority {

  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_MODERATOR("ROLE_MODERATOR"),
  ROLE_USER("ROLE_USER");

  final String name;

  @Override
  public String getAuthority() {
    return name;
  }
}
