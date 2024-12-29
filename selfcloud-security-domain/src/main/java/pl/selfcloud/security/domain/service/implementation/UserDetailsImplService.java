package pl.selfcloud.security.domain.service.implementation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.selfcloud.security.domain.model.User;
import pl.selfcloud.security.domain.repository.UserRepository;

@Service
public class UserDetailsImplService implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired
  public UserDetailsImplService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);
    return user.orElseThrow(
        () -> new UsernameNotFoundException("User with email " + email + " not found")
    );
  }
}