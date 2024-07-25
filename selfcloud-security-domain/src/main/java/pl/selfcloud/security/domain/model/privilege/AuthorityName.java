package pl.selfcloud.security.domain.model.privilege;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum AuthorityName implements GrantedAuthority {

  CREATE_ORDER("CREATE_ORDER"),
  UPDATE_ORDER("UPDATE_ORDER"),
  DELETE_ORDER("DELETE_ORDER"),
  READ_ORDER("READ_ORDER"),
  CREATE_USER("CREATE_USER"),
  DELETE_USER("DELETE_USER"),
  GRAND_AUTHORITY("GRAND_AUTHORITY"),
  REVOKE_AUTHORITY("REVOKE_AUTHORITY"),
  CREATE_ANNOUNCEMENT("CREATE_ANNOUNCEMENT"),
  UPDATE_ANNOUNCEMENT("UPDATE_ANNOUNCEMENT"),
  DELETE_ANNOUNCEMENT("DELETE_ANNOUNCEMENT"),
  READ_ANNOUNCEMENT("READ_ANNOUNCEMENT");

  final String name;

  @Override
  public String getAuthority() {
    return name;
  }

}
