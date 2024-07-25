package pl.selfcloud.security.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.api.dto.LoginUserDto;
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
  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
      TokenService tokenService) {
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
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String uuid = Utilities.generateUuid();
    String externalToken = jwtUtil.generateToken(User.builder()
        .email(uuid)
        .build());

    User user = (User) authResult.getPrincipal();

    String internalToken = Jwts.builder()
        .setSubject(authResult.getName())
        .claim("userId", user.getId())
        .claim("authorities", authResult.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC)))
        .signWith(SignatureAlgorithm.HS256, "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b")
        .compact();

    TokenEntity tokensEntity = TokenEntity.builder()
        .id(uuid)
        .token(internalToken)
        .username(authResult.getName())
        .createdBy("SYSTEM").createdOn(LocalDateTime.now())
        .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
        .build();

    tokenService.save(tokensEntity);
    generateResponse(externalToken, response);

  }

  private void generateResponse(String token, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"token\": \"" + token + "\",");
    response.getWriter().write("\"expiresIn\": \"" + jwtUtil.getExpirationTime() + "\"}");
    response.getWriter().flush();
  }

}