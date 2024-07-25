package pl.selfcloud.security.domain.service;


import io.eventuate.tram.sagas.orchestration.SagaManager;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.selfcloud.common.model.dto.RegistrationDto;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.api.dto.CreatedUserDto;
import pl.selfcloud.security.api.dto.LoginUserDto;
import pl.selfcloud.security.api.state.RejectionReason;
import pl.selfcloud.security.domain.model.User;
import pl.selfcloud.security.domain.model.privilege.Role;
import pl.selfcloud.security.domain.model.privilege.RoleName;
import pl.selfcloud.security.domain.repository.RoleRepository;
import pl.selfcloud.security.domain.repository.UserRepository;
import pl.selfcloud.security.domain.service.exception.AccountWithThisEmailExistsException;
import pl.selfcloud.security.domain.service.exception.InvalidEmailFormatException;
import pl.selfcloud.security.domain.service.publisher.UserDomainEventPublisher;
import pl.selfcloud.security.saga.user.create.CreateUserSagaState;


@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final UserDomainEventPublisher userDomainEventPublisher;
  private final SagaManager<CreateUserSagaState> sagaManager;

  private final JwtUtil jwtUtil;
  private final Role userRole;

//  private CreateUserSagaCompletionListener sagaCompletionListener;
  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository,
      PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
      UserDomainEventPublisher userDomainEventPublisher,
      SagaManager<CreateUserSagaState> sagaManager,
      JwtUtil jwtUtil
//      CreateUserSagaCompletionListener sagaCompletionListener
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.sagaManager = sagaManager;
    this.jwtUtil = jwtUtil;
//    this.sagaCompletionListener = sagaCompletionListener;
    this.userRole = roleRepository.findByName(RoleName.ROLE_USER).get();
    this.userDomainEventPublisher = userDomainEventPublisher;
  }


  @Transactional

//  public UserState signup(final RegistrationDto registrationDto)
//  public CompletableFuture<UserState> signup(final RegistrationDto registrationDto)
  public CreatedUserDto signup(final RegistrationDto registrationDto)
      throws ExecutionException, InterruptedException, TimeoutException {

    String email = registrationDto.getCustomerDto().getEmail();

    if(!isValidEmailAddress(email)){
      throw new InvalidEmailFormatException(email);
    }

    if(userRepository.existsByEmail(email)){
      throw new AccountWithThisEmailExistsException(email);
    }

    User user = User.create(
        registrationDto.getCustomerDto(),
        passwordEncoder.encode(registrationDto.getPassword()),
        userRole
    );

    User createdUser = userRepository.save(user);
    System.out.println("User created: " + createdUser.getId());

    CreateUserSagaState data = new CreateUserSagaState(createdUser.getId(), registrationDto);

    sagaManager.create(data, RegistrationDto.class, createdUser.getId());
    System.out.println("Saga created");

    User updatedUser = userRepository.findById(createdUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    System.out.println("User updated: " + updatedUser.getId());

    return new CreatedUserDto(updatedUser.getEmail(), updatedUser.getState());
  }

  @Transactional
  public String authenticate(LoginUserDto input) {

    Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

    if (optionalUser.isEmpty()){
      throw new UsernameNotFoundException("Username not found");
    }

    User user = optionalUser.get();
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            input.getEmail(),
            input.getPassword()
        )
    );
    return jwtUtil.generateToken(user);

  }

  public void approveUser(final Long userId){
    userRepository.findById(userId).get().approve();
  }

  public void rejectUser(final Long userId, RejectionReason rejectionReason){
    userRepository.findById(userId).get().reject(rejectionReason);
  }

  private static boolean isValidEmailAddress(final String email) {
    try {
      InternetAddress emailAddress = new InternetAddress(email);
      emailAddress.validate();
    } catch (AddressException ex) {
      return false;
    }
    return true;
  }

//  @Transactional
//  public ConnValidationResponse valid(final String token) {
//
//    String tokenJwt = token.split(" ")[1].trim();
//    String mail = jwtUtil.extractUsername(tokenJwt);
//
//    User user = userRepository.findByEmail(mail)
//        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//
//    Collection<GrantedAuthority> authorities = GrantedAuthoritiesMapper.mapRolesToGrantedAuthorities(user.getRoles());
//
//
//
//    return ConnValidationResponse.builder()
//        .roles(user.getRoles())
//        .username(user.getEmail())
//        .isAuthenticated(true)
//        .token(token)
//        .build();
//  }
}