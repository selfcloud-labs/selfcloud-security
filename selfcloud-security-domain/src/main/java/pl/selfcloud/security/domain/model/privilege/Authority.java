package pl.selfcloud.security.domain.model.privilege;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Getter
@Table(name = "authorities")
public class Authority implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  private AuthorityName name;

  private String description;

  public Authority(String name, String description){
    this.name = AuthorityName.valueOf(name);
  }

  public Authority(AuthorityName name, String description){
    this.name = name;
  }

  public Authority(String name){
    this.name = AuthorityName.valueOf(name);
  }

  public Authority(AuthorityName name){
    this.name = name;
  }
}