package pl.selfcloud.security.api.response;

import java.io.Serializable;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ConnValidationResponse implements Serializable {
  private String status;
  private boolean isAuthenticated;
  private String methodType;
  private String username;
  private String token;
  private Long userId;
  private Collection<TransferGrantedAuthority> authorities;
}