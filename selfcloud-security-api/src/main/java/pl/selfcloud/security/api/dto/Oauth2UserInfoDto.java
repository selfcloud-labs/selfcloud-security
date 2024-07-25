package pl.selfcloud.security.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Oauth2UserInfoDto {

  private final String name;
  private final String id;
  private final String email;

}