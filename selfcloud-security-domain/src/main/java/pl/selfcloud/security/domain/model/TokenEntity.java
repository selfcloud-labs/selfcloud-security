package pl.selfcloud.security.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.security.api.state.TokenState;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class TokenEntity {

  @Id
  private String id;
  private String email;
  private String token;
  private String modifiedBy;
  private LocalDateTime modifiedOn;
  private String createdBy;
  private LocalDateTime createdOn;
  @Enumerated(EnumType.STRING)
  private TokenState state;
}
