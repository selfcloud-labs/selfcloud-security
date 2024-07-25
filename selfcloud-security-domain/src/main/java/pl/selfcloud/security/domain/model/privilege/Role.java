package pl.selfcloud.security.domain.model.privilege;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private RoleName name;

  private String description;

  @ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
  @JoinTable(
      name = "ROLES_AUTHORITIES",
      joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "authority_id", referencedColumnName = "id"))
  private List<Authority> authorities;

  public Role(String name, List<Authority> authorities, String description){
    this.name = RoleName.valueOf(name);
    this.authorities = authorities;
    this.description = description;
  }

  public Role(RoleName name, List<Authority> authorities, String description){
    this.name = name;
    this.authorities = authorities;
    this.description = description;
  }


}