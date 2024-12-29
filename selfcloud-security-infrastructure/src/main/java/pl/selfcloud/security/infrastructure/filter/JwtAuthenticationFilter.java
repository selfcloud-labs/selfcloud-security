package pl.selfcloud.security.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import pl.selfcloud.security.api.dto.LoginUserDto;
import pl.selfcloud.security.api.state.TokenState;
import pl.selfcloud.security.api.util.JwtUtil;
import pl.selfcloud.security.api.util.Utilities;
import pl.selfcloud.security.domain.model.TokenEntity;
import pl.selfcloud.security.domain.model.User;
import pl.selfcloud.security.domain.service.TokenService;


@Slf4j
@Component
public class JwtAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final ObjectMapper mapper = new ObjectMapper();
  private final TokenService tokenService;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
      JwtUtil jwtUtil, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.tokenService = tokenService;
    this.setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginUserDto loginUserDto = mapper.readValue(request.getInputStream(), LoginUserDto.class);
      Authentication authentication = new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());
      return authenticationManager.authenticate(authentication);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    User user = (User) authResult.getPrincipal();
    String uuid = Utilities.generateUuid();

    String externalToken = generateExternalToken(uuid);
    String internalToken = generateInternalToken(user);

    TokenEntity tokenEntity = TokenEntity.builder()
        .id(uuid)
        .token(internalToken)
        .email(user.getEmail())
        .createdBy("SYSTEM").createdOn(LocalDateTime.now())
        .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
        .state(TokenState.LOGGED_IN)
        .build();

    tokenService.checkAndSave(tokenEntity);
    log.info("User with email " + user.getEmail() + " logged in.");
    generateResponse(externalToken, response);
  }

  private void generateResponse(String token, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"token\": \"" + token + "\",");
    response.getWriter().write("\"expiresIn\": \"" + jwtUtil.getExpirationTime() + "\"}");
    response.getWriter().flush();
  }

  private String generateExternalToken(final String uuid){
    return jwtUtil.generateToken(User.builder().email(uuid).build());
  }

  private String generateInternalToken(final User user){

    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("userId", user.getId());
    extraClaims.put("authorities", user.getAuthorities());

    return jwtUtil.generateToken(extraClaims, user);

  }
}