package pl.selfcloud.security.infrastructure.config;


import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.domain.service.TokenService;
import pl.selfcloud.security.domain.service.handler.OAuth2LoginSuccessHandler;
import pl.selfcloud.security.infrastructure.filter.JwtAuthenticationFilter;
import pl.selfcloud.security.infrastructure.filter.JwtAuthorizationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig  {

  private final static String[] staticResources  =  {
      "/static/**",
      "/css/**",
      "/images/**",
  };
  private final static String[] developerAccess = {
      "/h2-console/**"
  };


  @Autowired
  private final DefaultOAuth2UserService oAuth2UserService;
  @Autowired
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  @Autowired
  private final AuthenticationConfiguration authenticationConfiguration;
  @Autowired
  private final TokenService tokenService;
  @Autowired
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;
  @Autowired
  private final JwtUtil jwtUtil;
  @Autowired
  private final JwtAuthorizationFilter jwtAuthorizationFilter;


  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {



    http.authorizeHttpRequests(auth -> auth
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .requestMatchers(staticResources).permitAll()
            .requestMatchers(developerAccess).permitAll()
            .requestMatchers("api/v1/auth/login").permitAll()
            .requestMatchers("api/v1/auth/signup").permitAll()
            .requestMatchers("api/v1/auth/valid").authenticated()
            .requestMatchers("/oauth2/**").permitAll()
            .requestMatchers("/home").permitAll()
            .requestMatchers("/home/**").authenticated()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
        )

        .exceptionHandling(auth -> auth.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())

        .addFilter(jwtAuthenticationFilter(authenticationManager(authenticationConfiguration)))
        .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class)

        .formLogin(Customizer.withDefaults())
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler)
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
        );

    http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")
        .disable());
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, jwtUtil, tokenService);
    filter.setAuthenticationManager(authenticationManager);
    filter.setFilterProcessesUrl("/api/v1/auth/login");
    return filter;
  }
}