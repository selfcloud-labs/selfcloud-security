package pl.selfcloud.security.domain.service.mapper;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import pl.selfcloud.security.domain.model.privilege.Authority;
import pl.selfcloud.security.domain.model.privilege.Role;

public final class GrantedAuthoritiesMapper {
  public static Collection<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {

    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    for (Role role : roles) {
      grantedAuthorities.add(role.getName());
      for (Authority authority : role.getAuthorities()){
        grantedAuthorities.add(authority.getName());
      }
    }

    return grantedAuthorities;
  }
}
