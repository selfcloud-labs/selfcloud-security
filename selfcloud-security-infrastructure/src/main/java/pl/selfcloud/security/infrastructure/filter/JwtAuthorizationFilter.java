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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.api.util.SecurityConstants;
import pl.selfcloud.security.api.util.Utilities;
import pl.selfcloud.security.domain.model.TokenEntity;
import pl.selfcloud.security.domain.service.TokenService;


@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Autowired
  private final TokenService tokensRedisService;
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {

    String bearerToken = request.getHeader(SecurityConstants.AUTHORIZATION.name());

   if (!(Utilities.validString(bearerToken) && bearerToken.startsWith("Bearer"))) {
     filterChain.doFilter(request, response);
     return;
   }

   String uuid = jwtUtil.extractUsername(bearerToken.split(" ")[1].trim());

    TokenEntity tokenEntity = null;
    try {
      tokenEntity = tokensRedisService.getTokenById(uuid);
    } catch (RuntimeException exception){
        exception.printStackTrace();
      filterChain.doFilter(request, response);
    }

    String internalToken = tokenEntity.getToken();
    List<String> authorities = jwtUtil.extractClaim(internalToken, claims -> claims.get("authorities", List.class));
    Long userId = jwtUtil.extractClaim(internalToken, claims -> claims.get("userId", Long.class));

    List<GrantedAuthority> grantedAuthorities = authorities.stream().map(
        SimpleGrantedAuthority::new).collect(
        Collectors.toList());
    Authentication authentication = new UsernamePasswordAuthenticationToken(tokenEntity.getUsername(), null, grantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    request.setAttribute("username", tokenEntity.getUsername());
    request.setAttribute("authorities", authorities);
    request.setAttribute("userId", userId);

    filterChain.doFilter(request, response);

  }
}
