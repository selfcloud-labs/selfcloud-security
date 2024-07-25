package pl.selfcloud.security.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@RedisHash(value = "Tokens", timeToLive = 86400)
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
  private String username;
  private String token;
  private String modifiedBy;
  private LocalDateTime modifiedOn;
  private String createdBy;
  private LocalDateTime createdOn;
}
