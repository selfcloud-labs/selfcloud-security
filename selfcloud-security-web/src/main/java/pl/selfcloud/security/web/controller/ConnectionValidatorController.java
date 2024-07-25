package pl.selfcloud.security.web.controller;


import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.api.response.ConnValidationResponse;
import pl.selfcloud.security.domain.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class ConnectionValidatorController {
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final UserDetailsService userDetailsService;

  public ConnectionValidatorController(JwtUtil jwtUtil, UserService userService,
      UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
    this.userDetailsService = userDetailsService;
  }

  @GetMapping(value = "/validateToken", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {

    String token = request.getHeader("authorization");
    String username = (String) request.getAttribute("username");
    List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) request.getAttribute("authorities");
    Long userId = (Long) request.getAttribute("userId");

    return ResponseEntity.ok(ConnValidationResponse.builder()
        .token(token)
        .username(username)
        .authorities(grantedAuthorities)
        .userId(userId)
        .methodType("GET")
        .status("200")
        .isAuthenticated(true)
        .build()
    );

  }
//  @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//  public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {
//    String username = (String) request.getAttribute("username");
//    String token = (String) request.getAttribute("jwt");
//    List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) request.getAttribute("authorities");
//    return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
//        .username(username).token(token).authorities(grantedAuthorities)
//        .isAuthenticated(true).build());
//  }

}
