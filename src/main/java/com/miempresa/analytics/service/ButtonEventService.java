package com.miempresa.analytics.service;

import com.miempresa.analytics.dto.ButtonEventRequest;
import com.miempresa.analytics.dto.ButtonEventResponse;
import com.miempresa.analytics.dto.ButtonMonthlyStat;
import com.miempresa.analytics.dto.PageResponse;
import com.miempresa.analytics.model.ButtonEvent;
import com.miempresa.analytics.repository.ButtonEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ButtonEventService {

    private final ButtonEventRepository repository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    // Inyección de dependencias mediante constructor (Spring Boot best practice)
    public ButtonEventService(ButtonEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida y guarda un evento de botón.
     * Valida que type y buttonId no estén vacíos.
     * 
     * @param request DTO con los datos del evento
     * @throws IllegalArgumentException si type o buttonId están vacíos
     */
    @Transactional
    public void save(ButtonEventRequest request) {
        // Validación de campos obligatorios
        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("type is required");
        }
        if (request.getButtonId() == null || request.getButtonId().trim().isEmpty()) {
            throw new IllegalArgumentException("buttonId is required");
        }

        // Mapeo del DTO a la entidad
        ButtonEvent event = new ButtonEvent();
        event.setType(request.getType().trim());
        event.setButtonId(request.getButtonId().trim());
        
        // appId es opcional
        if (request.getAppId() != null && !request.getAppId().trim().isEmpty()) {
            event.setAppId(request.getAppId().trim());
        }
        
        // Campos opcionales
        if (request.getRoute() != null && !request.getRoute().trim().isEmpty()) {
            event.setRoute(request.getRoute().trim());
        }
        if (request.getUserId() != null && !request.getUserId().trim().isEmpty()) {
            event.setUserId(request.getUserId().trim());
        }
        if (request.getMetadataJson() != null && !request.getMetadataJson().trim().isEmpty()) {
            event.setMetadata(request.getMetadataJson().trim());
        }

        // Coordenadas y ancho de pantalla (opcionales)
        if (request.getCoordinateX() != null) {
            event.setCoordinateX(request.getCoordinateX());
        }
        if (request.getCoordinateY() != null) {
            event.setCoordinateY(request.getCoordinateY());
        }
        if (request.getScreenWidth() != null) {
            event.setScreenWidth(request.getScreenWidth());
        }

        // createdAt: usar el del request si viene, sino usar fecha/hora actual
        if (request.getCreatedAt() != null && !request.getCreatedAt().trim().isEmpty()) {
            try {
                LocalDateTime parsedDate = LocalDateTime.parse(request.getCreatedAt(), ISO_FORMATTER);
                event.setCreatedAt(parsedDate);
            } catch (DateTimeParseException e) {
                // Si no se puede parsear, usar fecha actual
                event.setCreatedAt(LocalDateTime.now());
            }
        } else {
            event.setCreatedAt(LocalDateTime.now());
        }

        // Guardar en la base de datos
        repository.save(event);
    }

    /**
     * Obtiene estadísticas mensuales agrupadas por button_id, type y mes.
     * 
     * @return Lista de estadísticas mensuales
     */
    @Transactional(readOnly = true)
    public List<ButtonMonthlyStat> getMonthlyStats() {
        List<Object[]> results = repository.findMonthlyStats();
        List<ButtonMonthlyStat> stats = new ArrayList<>();

        for (Object[] row : results) {
            String buttonId = (String) row[0];
            String type = (String) row[1];
            String monthStr = (String) row[2]; // Formato: "YYYY-MM-01"
            Long totalClicks = ((Number) row[3]).longValue();

            // Parsear el mes a LocalDate
            LocalDate month = LocalDate.parse(monthStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ButtonMonthlyStat stat = new ButtonMonthlyStat(buttonId, type, month, totalClicks);
            stats.add(stat);
        }

        return stats;
    }

    /**
     * Obtiene todos los eventos con paginado.
     * 
     * @param page Número de página (0-indexed)
     * @param size Tamaño de la página
     * @return Respuesta paginada con eventos
     */
    @Transactional(readOnly = true)
    public PageResponse<ButtonEventResponse> getAllEvents(int page, int size) {
        // Validar parámetros de paginación
        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = 10;
        }
        if (size > 100) {
            size = 100; // Límite máximo
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ButtonEvent> eventPage = repository.findAll(pageable);

        // Mapear entidades a DTOs
        List<ButtonEventResponse> content = eventPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                eventPage.getNumber(),
                eventPage.getSize(),
                eventPage.getTotalElements()
        );
    }

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento
     * @return Evento encontrado o null si no existe
     */
    @Transactional(readOnly = true)
    public Optional<ButtonEventResponse> getEventById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse);
    }

    /**
     * Mapea una entidad ButtonEvent a un DTO ButtonEventResponse.
     */
    private ButtonEventResponse mapToResponse(ButtonEvent event) {
        return new ButtonEventResponse(
                event.getId(),
                event.getType(),
                event.getAppId(),
                event.getButtonId(),
                event.getRoute(),
                event.getUserId(),
                event.getMetadata(),
                event.getCoordinateX(),
                event.getCoordinateY(),
                event.getScreenWidth(),
                event.getCreatedAt()
        );
    }
}

