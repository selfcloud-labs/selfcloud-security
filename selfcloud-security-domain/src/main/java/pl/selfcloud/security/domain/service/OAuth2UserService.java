package pl.selfcloud.security.domain.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.selfcloud.security.api.dto.Oauth2UserInfoDto;
import pl.selfcloud.security.domain.model.User;
import pl.selfcloud.security.domain.model.privilege.Role;
import pl.selfcloud.security.domain.model.privilege.RoleName;
import pl.selfcloud.security.domain.repository.RoleRepository;
import pl.selfcloud.security.domain.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    return processOAuth2User(oAuth2UserRequest, oAuth2User);
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    Oauth2UserInfoDto userInfoDto = Oauth2UserInfoDto.builder()
        .name(oAuth2User.getAttributes().get("name").toString())
        .id(oAuth2User.getAttributes().get("sub").toString())
        .email(oAuth2User.getAttributes().get("email").toString())
        .build();

    Optional<User> userOptional = userRepository.findByEmail(userInfoDto.getEmail());

    User user = userOptional
        .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
        .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));

    return new DefaultOAuth2User(user.getAuthorities(), oAuth2User.getAttributes(), "email");
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserInfoDto userInfoDto) {
    Optional<Role> role = roleRepository.findByName(RoleName.ROLE_USER);

    User user = User.builder()
        .email(userInfoDto.getEmail())
        .enabled(true)
        .credentialsNonExpired(true)
        .accountNonLocked(true)
        .accountNonExpired(true)
        .roles(List.of(role.get()))
        .build();

    User newUser = userRepository.save(user);
//    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser));
    return newUser;
  }

  private User updateExistingUser(User existingUser, Oauth2UserInfoDto userInfoDto) {
    existingUser.setEmail(userInfoDto.getEmail());
    return userRepository.save(existingUser);
  }
}