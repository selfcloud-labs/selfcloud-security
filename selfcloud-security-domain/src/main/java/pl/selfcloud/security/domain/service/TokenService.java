package pl.selfcloud.security.domain.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.selfcloud.security.domain.model.TokenEntity;
import pl.selfcloud.security.domain.repository.TokenRepository;
import pl.selfcloud.security.domain.service.exception.TokenNotFoundException;

@Service
@AllArgsConstructor
public class TokenService {


  @Autowired
  private TokenRepository tokenRepository;

  @Transactional
  public TokenEntity checkAndSave(final TokenEntity entity) {
//    tokenRepository.deleteByEmail(entity.getEmail());
    return tokenRepository.save(entity);
  }

//  @Cacheable(cacheNames="getTokenById")
  public TokenEntity getTokenById(String id) {
    Optional<TokenEntity> tokenEntity = tokenRepository.findById(id);
    if (tokenEntity.isEmpty()) throw new TokenNotFoundException(id);
    return tokenEntity.get();
//    return tokenRepository.findById(id).orElseThrow(
//        () -> new TokenNotFoundException(id)
//    );
  }

  public boolean existsByEmail (final String email){
    return tokenRepository.existsByEmail(email);
  }


  public Iterable<TokenEntity> findAll() {
    return tokenRepository.findAll();
  }


}