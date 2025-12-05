package com.miempresa.analytics.controller;

import com.miempresa.analytics.dto.ApiResponse;
import com.miempresa.analytics.dto.UIEventRequest;
import com.miempresa.analytics.dto.UIEventResponse;
import com.miempresa.analytics.dto.UIMonthlyStat;
import com.miempresa.analytics.dto.PageResponse;
import com.miempresa.analytics.service.UIEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@Validated
@Tag(name = "UI Events", description = "API para gestión de eventos de interacción con elementos de UI")
public class UIEventController {

    private final UIEventService service;

    // Inyección de dependencias mediante constructor (Spring Boot best practice)
    public UIEventController(UIEventService service) {
        this.service = service;
    }

    /**
     * Endpoint para registrar un evento de interacción con un elemento de UI.
     * Requiere autenticación.
     */
    @PostMapping("/events")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Registrar evento de UI", 
               description = "Registra un nuevo evento de interacción con cualquier elemento de UI (botón, link, card, etc.). Requiere autenticación.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Evento registrado correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "No autenticado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", 
            description = "No autorizado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Object>> createEvent(
            @Valid @RequestBody UIEventRequest request) {
        try {
            service.save(request);
            return ResponseEntity.ok(ApiResponse.success("Event registered successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todos los eventos con paginado.
     * No requiere autenticación (solo lectura).
     */
    @GetMapping("/events")
    @Operation(summary = "Obtener eventos paginados", 
               description = "Retorna una lista paginada de todos los eventos de UI")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Lista de eventos obtenida correctamente",
            content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    public ResponseEntity<ApiResponse<PageResponse<UIEventResponse>>> getAllEvents(
            @Parameter(description = "Número de página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResponse<UIEventResponse> events = service.getAllEvents(page, size);
            return ResponseEntity.ok(ApiResponse.success(events));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving events: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener un evento por su ID.
     * No requiere autenticación (solo lectura).
     */
    @GetMapping("/events/{id}")
    @Operation(summary = "Obtener evento por ID", 
               description = "Retorna un evento específico por su ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Evento encontrado",
            content = @Content(schema = @Schema(implementation = UIEventResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Evento no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<UIEventResponse>> getEventById(
            @Parameter(description = "ID del evento", example = "1")
            @PathVariable Long id) {
        try {
            Optional<UIEventResponse> event = service.getEventById(id);
            if (event.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(event.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Event not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving event: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener estadísticas mensuales de clicks por elemento y tipo.
     * No requiere autenticación (solo lectura).
     */
    @GetMapping("/stats/monthly")
    @Operation(summary = "Obtener estadísticas mensuales", 
               description = "Retorna agregados mensuales de clicks por elemento, tipo y mes")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Estadísticas obtenidas correctamente",
            content = @Content(schema = @Schema(implementation = UIMonthlyStat.class)))
    })
    public ResponseEntity<ApiResponse<List<UIMonthlyStat>>> getMonthlyStats() {
        try {
            List<UIMonthlyStat> stats = service.getMonthlyStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving statistics: " + e.getMessage()));
        }
    }
}

