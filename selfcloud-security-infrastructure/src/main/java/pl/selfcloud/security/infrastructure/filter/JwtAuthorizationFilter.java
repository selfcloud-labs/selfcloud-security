package pl.selfcloud.security.infrastructure.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.selfcloud.security.api.util.JwtUtil;
import pl.selfcloud.security.api.util.SecurityConstants;
import pl.selfcloud.security.api.util.Utilities;
import pl.selfcloud.security.domain.model.TokenEntity;
import pl.selfcloud.security.domain.service.TokenService;


@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Autowired
  private final TokenService tokenService;
  @Autowired
  private final UserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {

    String externalBearerToken = request.getHeader(SecurityConstants.AUTHORIZATION.name());

    if (!(Utilities.validString(externalBearerToken) && externalBearerToken.startsWith("Bearer"))) {
      filterChain.doFilter(request, response);
      return;
    }

    String externalToken = externalBearerToken.split(" ")[1].trim();
    String uuid = jwtUtil.extractUsername(externalToken);

    TokenEntity tokenEntity = tokenService.getTokenById(uuid);
    String internalToken = tokenEntity.getToken();

    String email = jwtUtil.extractUsername(internalToken);
    UserDetails user = userDetailsService.loadUserByUsername(email);

    if(jwtUtil.isTokenValid(externalToken, user)){
      throw  new AuthorizationServiceException("");
    }

    List<String> authorities = jwtUtil.extractClaim(internalToken, claims -> claims.get("authorities", List.class));
    Long userId = jwtUtil.extractClaim(internalToken, claims -> claims.get("userId", Long.class));

    List<GrantedAuthority> grantedAuthorities = authorities.stream().map(
        SimpleGrantedAuthority::new).collect(
            Collectors.toList());
    Authentication authentication = new UsernamePasswordAuthenticationToken(tokenEntity.getEmail(), null, grantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    request.setAttribute("username", tokenEntity.getEmail());
    request.setAttribute("authorities", authorities);
    request.setAttribute("userId", userId);

    logger.info("username: " + tokenEntity.getEmail() + ", authorities: " + authorities);
    filterChain.doFilter(request, response);

  }
}
