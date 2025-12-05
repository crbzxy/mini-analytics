package com.miempresa.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuración de seguridad para proteger endpoints de modificación.
 * Solo los endpoints POST requieren autenticación.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configuración de seguridad HTTP.
     * Permite acceso público a GET, pero requiere autenticación para POST.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Deshabilitar CSRF para APIs REST
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Permitir acceso público a Swagger
                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                // Permitir acceso público a endpoints GET (solo lectura)
                .antMatchers("/v1/events/**", "/v1/stats/**").permitAll()
                // Requerir autenticación para POST (modificación)
                .antMatchers("/v1/events").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .httpBasic(); // Autenticación básica HTTP
    }

    /**
     * Configuración de usuarios en memoria.
     * En producción, esto debería conectarse a una base de datos o servicio de autenticación.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Codificador de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

