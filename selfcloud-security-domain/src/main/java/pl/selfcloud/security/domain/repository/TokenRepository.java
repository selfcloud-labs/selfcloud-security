package pl.selfcloud.security.domain.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.security.domain.model.TokenEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, String> {

  Optional<TokenEntity> findById(String id);
}