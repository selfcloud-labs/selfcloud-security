package pl.selfcloud.security.api.response;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Setter;


public class LoginResponse {

  @Setter
  private String token;
  @Setter
  private long expiresIn;

  private HttpServletResponse response;

  public HttpServletResponse getResponse() throws IOException {
    response.setContentType("application/json");
    response.getWriter().write("{\"token\": \"" + token + "\",");
    response.getWriter().write("\"expiresIn\": \"" + expiresIn + "\"}");
    response.getWriter().flush();
    return response;
  }
}