package com.miempresa.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación Spring Boot.
 * 
 * @SpringBootApplication incluye:
 * - @Configuration: Define esta clase como fuente de configuración
 * - @EnableAutoConfiguration: Habilita la auto-configuración de Spring Boot
 * - @ComponentScan: Escanea componentes en el paquete base y subpaquetes
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.miempresa.analytics.repository")
@EntityScan(basePackages = "com.miempresa.analytics.model")
public class UIAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UIAnalyticsServiceApplication.class, args);
    }
}

