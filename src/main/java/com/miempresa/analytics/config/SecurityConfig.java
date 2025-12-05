package com.miempresa.analytics.config;

import com.miempresa.analytics.security.JwtAuthenticationFilter;
import com.miempresa.analytics.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuración de seguridad para proteger endpoints de modificación.
 * Solo los endpoints POST requieren autenticación.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    // Variables de entorno para usuario regular
    @Value("${auth.users.user.username:analytics_user_dev}")
    private String userUsername;

    @Value("${auth.users.user.password:ChangeMe123!Secure}")
    private String userPassword;

    // Variables de entorno para usuario admin
    @Value("${auth.users.admin.username:analytics_admin_dev}")
    private String adminUsername;

    @Value("${auth.users.admin.password:ChangeMe456!Secure}")
    private String adminPassword;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configuración de seguridad HTTP.
     * Permite acceso público a GET, pero requiere autenticación JWT para POST.
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
                // Permitir acceso público al endpoint de login
                .antMatchers("/v1/auth/login").permitAll()
                // Permitir acceso público a endpoints GET (solo lectura)
                .antMatchers(HttpMethod.GET, "/v1/events/**", "/v1/stats/**").permitAll()
                // Requerir autenticación para POST (modificación)
                .antMatchers(HttpMethod.POST, "/v1/events").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Configuración de usuarios en memoria.
     * Las credenciales se leen desde variables de entorno o application.yml.
     * En producción, esto debería conectarse a una base de datos o servicio de
     * autenticación.
     * 
     * ⚠️ SEGURIDAD: Valida que no se usen credenciales débiles conocidas.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Validar credenciales débiles conocidas
        validateCredentials(userUsername, userPassword, "user");
        validateCredentials(adminUsername, adminPassword, "admin");

        UserDetails user = User.builder()
                .username(userUsername)
                .password(passwordEncoder().encode(userPassword))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Valida que las credenciales no sean débiles conocidas.
     * Rechaza credenciales comunes que representan un riesgo de seguridad.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param userType Tipo de usuario (para mensajes de error)
     * @throws IllegalStateException Si se detectan credenciales débiles
     */
    private void validateCredentials(String username, String password, String userType) {
        // Lista de credenciales débiles conocidas que deben ser rechazadas
        Set<String> weakUsernames = new HashSet<>(Arrays.asList("user", "admin", "test", "demo", "guest"));
        Set<String> weakPasswords = new HashSet<>(Arrays.asList("password", "admin", "123456", "12345678", 
                "1234", "qwerty", "abc123", "password123", "admin123", "root", "test", "demo"));

        if (weakUsernames.contains(username.toLowerCase())) {
            throw new IllegalStateException(
                String.format("⚠️ SEGURIDAD: El nombre de usuario '%s' para %s es demasiado débil y está prohibido. " +
                    "Por favor, configure una variable de entorno AUTH_%s_USERNAME con un valor más seguro.",
                    username, userType, userType.toUpperCase())
            );
        }

        if (weakPasswords.contains(password.toLowerCase())) {
            throw new IllegalStateException(
                String.format("⚠️ SEGURIDAD: La contraseña para %s es demasiado débil y está prohibida. " +
                    "Por favor, configure una variable de entorno AUTH_%s_PASSWORD con una contraseña más segura " +
                    "(mínimo 12 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales).",
                    userType, userType.toUpperCase())
            );
        }

        // Validación adicional: contraseñas muy cortas
        if (password.length() < 8) {
            throw new IllegalStateException(
                String.format("⚠️ SEGURIDAD: La contraseña para %s es demasiado corta (mínimo 8 caracteres). " +
                    "Por favor, configure una variable de entorno AUTH_%s_PASSWORD con una contraseña más segura.",
                    userType, userType.toUpperCase())
            );
        }
    }

    /**
     * Codificador de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea el filtro JWT como bean para evitar dependencia circular.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setJwtUtil(jwtUtil);
        filter.setUserDetailsService(userDetailsService());
        return filter;
    }
}
