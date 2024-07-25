package pl.selfcloud.security.infrastructure.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.domain.service.TokenService;
import pl.selfcloud.security.infrastructure.filter.JwtAuthorizationFilter;

@Configuration
@AllArgsConstructor
public class FilterConfig {

  @Autowired
  private final TokenService tokenService;
  @Autowired
  private final JwtUtil jwtUtil;
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter(){
    return new JwtAuthorizationFilter(tokenService, jwtUtil);
  }
}
