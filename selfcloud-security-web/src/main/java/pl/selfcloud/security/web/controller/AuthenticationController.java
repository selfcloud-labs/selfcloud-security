package pl.selfcloud.security.web.controller;


import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.selfcloud.security.api.dto.CreatedUserDto;
import pl.selfcloud.security.api.dto.RegistrationDto;
import pl.selfcloud.security.api.util.JwtUtil;
import pl.selfcloud.security.domain.service.UserService;
import pl.selfcloud.security.domain.service.exception.AccountWithThisEmailExistsException;

@RequestMapping("api/v1/auth")
@RestController
public class AuthenticationController {
  private final JwtUtil jwtUtil;
  private final UserService userService;


  public AuthenticationController(JwtUtil jwtUtil, UserService userService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<CreatedUserDto> register(@RequestBody RegistrationDto registrationDto)
      throws ExecutionException, InterruptedException, TimeoutException {

//    CompletableFuture<UserState> userState = userService.signup(registrationDto);
    CreatedUserDto user = userService.signup(registrationDto);
    return ResponseEntity.ok(user);
  }

//  @GetMapping("/logout")
//  public ResponseEntity<User> logout()



  @ExceptionHandler(AccountWithThisEmailExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<String> handleAccountWithThisEmailExistsException(AccountWithThisEmailExistsException exception) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(exception.getMessage());
  }

  private void generateResponse(String token, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().write("{\"token\": \"" + token + "\",");
    response.getWriter().write("\"expiresIn\": \"" + jwtUtil.getExpirationTime() + "\"}");
    response.getWriter().flush();
  }
}