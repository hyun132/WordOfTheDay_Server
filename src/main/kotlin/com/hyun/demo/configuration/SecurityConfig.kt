package com.hyun.demo.configuration

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain{
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } } // H2 콘솔 iframe 허용
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/")
                    .permitAll()
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    )
                    .permitAll()
                    .anyRequest()
                    .permitAll()
            }
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .build()
    }
}