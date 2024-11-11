package com.example.UserAuthentication.springSecurity;

import com.example.UserAuthentication.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@NoArgsConstructor
@AllArgsConstructor
public class securityConfig {

    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/register").permitAll() // Allow registration
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .httpBasic(Customizer.withDefaults()); // Enable Basic Authentication

        // Disable CSRF protection for APIs (be cautious in production)
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);



        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Use PasswordEncoder

        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder bean
    }

}
