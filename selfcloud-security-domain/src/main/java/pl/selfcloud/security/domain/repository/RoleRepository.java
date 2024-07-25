package pl.selfcloud.security.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.security.domain.model.privilege.Role;
import pl.selfcloud.security.domain.model.privilege.RoleName;

@Repository
public interface  RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName name);
}
