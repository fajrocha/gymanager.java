package com.faroc.gymanager.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/authentication/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
