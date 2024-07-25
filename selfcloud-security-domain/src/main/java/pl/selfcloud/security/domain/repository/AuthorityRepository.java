package pl.selfcloud.security.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pl.selfcloud.security.domain.model.privilege.Authority;
import pl.selfcloud.security.domain.model.privilege.AuthorityName;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
  Optional<Authority> findByName(AuthorityName name);
}

