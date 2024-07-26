package pl.selfcloud.security.domain.service.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.selfcloud.security.api.util.JwtUtil;


@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired
  private final JwtUtil jwtUtil;

  public OAuth2LoginSuccessHandler(JwtUtil jwtService) {
    this.jwtUtil = jwtService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();


    response.setContentType("application/json");
    response.getWriter().write("{\"token\": \"" + jwtUtil.generateToken(oauthUser) + "\",");
    response.getWriter().write("\"expiresIn\": \"" + jwtUtil.getExpirationTime() + "\"}");
    response.getWriter().flush();
  }

}