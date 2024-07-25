package pl.selfcloud.security.infrastructure.config;


//import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.selfcloud.common.security.util.JwtUtil;

@Configuration
@AllArgsConstructor
//@Import(OptimisticLockingDecoratorConfiguration.class)
public class ApplicationConfiguration {

  @Bean
  public JwtUtil jwtUtil(){
    return new JwtUtil();
  }

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("getTokenById");
  }

}