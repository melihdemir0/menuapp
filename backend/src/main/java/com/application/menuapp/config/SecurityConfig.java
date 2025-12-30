package com.application.menuapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.application.menuapp.repository.UserRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final UserRepository userRepository;

  public SecurityConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      com.application.menuapp.entity.User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + username));

      String dbPass = user.getPassword(); 
 
      if (dbPass != null && !dbPass.startsWith("{")) {
        dbPass = "{noop}" + dbPass;
      }

      
      return User.withUsername(user.getUsername())
          .password(dbPass)
          .roles(user.getRole())
          .build();
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors(Customizer.withDefaults())
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/files/**").hasRole("ADMIN")
        .requestMatchers("/", "/menu/**", "/categories/**", "/css/**", "/js/**", "/images/**").permitAll()
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/api/auth/login").permitAll()
        .requestMatchers("/login").permitAll()
        .requestMatchers("/api/auth/me").authenticated()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .defaultSuccessUrl("http://localhost:5173/", true)
        .permitAll()
      )
      .logout(logout -> logout
        .logoutSuccessUrl("http://localhost:5173/")
        .permitAll()
      );

    return http.build();
  }
}
