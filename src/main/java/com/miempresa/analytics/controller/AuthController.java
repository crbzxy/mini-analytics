package com.miempresa.analytics.controller;

import com.miempresa.analytics.dto.ApiResponse;
import com.miempresa.analytics.dto.LoginRequest;
import com.miempresa.analytics.dto.LoginResponse;
import com.miempresa.analytics.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@Validated
@Tag(name = "Authentication", description = "API para autenticación con JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    // Inyección de dependencias mediante constructor (Spring Boot best practice)
    public AuthController(AuthenticationManager authenticationManager,
                         UserDetailsService userDetailsService,
                         JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", 
               description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        
        try {
            // Autenticar usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // Cargar detalles del usuario
            final UserDetails userDetails = userDetailsService.loadUserByUsername(
                loginRequest.getUsername()
            );

            // Generar token
            final String token = jwtUtil.generateToken(userDetails);
            final Long expiresIn = jwtUtil.getExpiration() / 1000; // convertir a segundos

            LoginResponse loginResponse = new LoginResponse(
                token, 
                userDetails.getUsername(), 
                expiresIn
            );

            return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", loginResponse)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse<>(false, "Invalid username or password", null)
            );
        }
    }
}

