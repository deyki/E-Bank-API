package com.EBank.EBankApplication.security;

import com.EBank.EBankApplication.service.impl.AuthServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.OutputStream;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/api/admin/**").hasAuthority("ADMIN");
                    auth.requestMatchers("/api/user/**").hasAuthority("USER");
                    auth.anyRequest().authenticated();
                })

                .userDetailsService(authService)

                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    OutputStream outputStream = response.getOutputStream();
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(outputStream, "Unauthorized");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }))

                .exceptionHandling(exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    OutputStream outputStream = response.getOutputStream();
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(outputStream, "Forbidden");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                }))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
