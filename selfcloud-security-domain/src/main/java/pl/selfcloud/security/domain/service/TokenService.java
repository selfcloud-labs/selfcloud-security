package pl.selfcloud.security.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.selfcloud.security.domain.model.TokenEntity;
import pl.selfcloud.security.domain.repository.TokenRepository;

@Service
@AllArgsConstructor
public class TokenService {


  @Autowired
  private TokenRepository tokenRepository;

  public TokenEntity save(TokenEntity entity) {
    return tokenRepository.save(entity);
  }

  @Cacheable(cacheNames="getTokenById")
  public TokenEntity getTokenById(String id) {
    return tokenRepository.findById(id).orElseThrow(
        () -> new AccessDeniedException("Token not found")
    );
  }

  public Iterable<TokenEntity> findAll() {
    return tokenRepository.findAll();
  }


}