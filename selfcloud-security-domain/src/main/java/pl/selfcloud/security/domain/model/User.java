package pl.selfcloud.security.domain.model;

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
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.selfcloud.customer.api.dto.CustomerDto;
import pl.selfcloud.security.api.state.RejectionReason;
import pl.selfcloud.security.api.state.UserState;
import pl.selfcloud.security.domain.model.privilege.Role;
import pl.selfcloud.security.domain.service.mapper.GrantedAuthoritiesMapper;

@Entity
@Data
@Getter
@Builder
@Setter
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String password;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;
  @Enumerated(EnumType.STRING)
  private RejectionReason rejectionReason;
  @Enumerated(EnumType.STRING)
  private UserState state;
  @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
  @JoinTable(
      name = "USERS_ROLES",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

  public User(Long id, String email, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked, RejectionReason rejectionReason, Collection<Role> roles) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.enabled = true;
    this.accountNonExpired = true;
    this.credentialsNonExpired = true;
    this.accountNonLocked = true;
    this.rejectionReason = null;
    this.state = UserState.PENDING;
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return GrantedAuthoritiesMapper.mapRolesToGrantedAuthorities(roles);
  }

  @Override
  public String getUsername(){
    return this.email;
  }

  public void approve(){
    this.state = UserState.APPROVED;
  }

  public void reject(final RejectionReason rejectionReason){
    this.state = UserState.REJECTED;
    this.rejectionReason = rejectionReason;
  }

  public void remove(){
    this.state = UserState.REMOVED;
  }

//  public static ResultWithDomainEvents<User, UserDomainEvent> createUserEvent(final User user) {
//    List<UserDomainEvent> events = Collections.singletonList(new UserCreatedEvent(user.getId(), "Lalalalala"));
//    return new ResultWithDomainEvents<>(user, events);
//  }

  public static User create(
      final CustomerDto customerDto,
      final String encodedPassword,
      final Role basicRole){
    return User.builder()
        .email(customerDto.getEmail())
        .password(encodedPassword)
        .enabled(true)
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .roles(List.of(basicRole))
        .state(UserState.PENDING)
        .rejectionReason(null)
        .build();
  }

}
