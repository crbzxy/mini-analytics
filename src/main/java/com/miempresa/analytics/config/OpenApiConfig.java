package com.miempresa.analytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Button Analytics Service API")
                        .version("1.0.0")
                        .description("Microservicio REST para analítica de eventos de clic en botones. " +
                                "Permite registrar eventos, consultar estadísticas y obtener eventos con paginado.")
                        .contact(new Contact()
                                .name("Mi Empresa")
                                .email("support@miempresa.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://miempresa.com")))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("http://localhost:8085")
                                .description("Servidor de producción")
                ));
    }
}

