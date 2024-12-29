package pl.selfcloud.security.api.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferGrantedAuthority implements GrantedAuthority, Serializable {

  private String role;

  @Override
  public String getAuthority() {
    return this.role;
  }

  public TransferGrantedAuthority(GrantedAuthority grantedAuthority){
    this.role = grantedAuthority.getAuthority();
  }
}
