package pl.selfcloud.security.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.security.domain.model.User;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {


  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

}
