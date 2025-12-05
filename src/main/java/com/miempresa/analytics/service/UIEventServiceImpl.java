package com.miempresa.analytics.service;

import com.miempresa.analytics.dto.UIEventRequest;
import com.miempresa.analytics.dto.UIEventResponse;
import com.miempresa.analytics.dto.UIMonthlyStat;
import com.miempresa.analytics.dto.PageResponse;
import com.miempresa.analytics.model.UIEvent;
import com.miempresa.analytics.repository.UIEventRepository;
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

/**
 * Implementación del servicio para gestión de eventos de UI.
 * Contiene la lógica de negocio para operaciones con eventos.
 */
@Service
public class UIEventServiceImpl implements UIEventService {

    private final UIEventRepository repository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    // Inyección de dependencias mediante constructor (Spring Boot best practice)
    public UIEventServiceImpl(UIEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida y guarda un evento de elemento UI.
     * Valida que type y elementId no estén vacíos.
     * 
     * @param request DTO con los datos del evento
     * @throws IllegalArgumentException si type o elementId están vacíos
     */
    @Override
    @Transactional
    public void save(UIEventRequest request) {
        // Validación de campos obligatorios
        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("type is required");
        }
        if (request.getElementId() == null || request.getElementId().trim().isEmpty()) {
            throw new IllegalArgumentException("elementId is required");
        }

        // Mapeo del DTO a la entidad
        UIEvent event = new UIEvent();
        event.setType(request.getType().trim());
        event.setElementId(request.getElementId().trim());
        
        // elementType es opcional (button, link, card, image, etc.)
        if (request.getElementType() != null && !request.getElementType().trim().isEmpty()) {
            event.setElementType(request.getElementType().trim());
        } else {
            // Valor por defecto si no se especifica
            event.setElementType("button");
        }
        
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
        if (request.getScreenHeight() != null) {
            event.setScreenHeight(request.getScreenHeight());
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
     * Obtiene estadísticas mensuales agrupadas por element_id, type y mes.
     * 
     * @return Lista de estadísticas mensuales
     */
    @Override
    @Transactional(readOnly = true)
    public List<UIMonthlyStat> getMonthlyStats() {
        List<Object[]> results = repository.findMonthlyStats();
        List<UIMonthlyStat> stats = new ArrayList<>();

        for (Object[] row : results) {
            String elementId = (String) row[0];
            String type = (String) row[1];
            String monthStr = (String) row[2]; // Formato: "YYYY-MM-01"
            Long totalClicks = ((Number) row[3]).longValue();

            // Parsear el mes a LocalDate
            LocalDate month = LocalDate.parse(monthStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            UIMonthlyStat stat = new UIMonthlyStat(elementId, type, month, totalClicks);
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
    @Override
    @Transactional(readOnly = true)
    public PageResponse<UIEventResponse> getAllEvents(int page, int size) {
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
        Page<UIEvent> eventPage = repository.findAll(pageable);

        // Mapear entidades a DTOs
        List<UIEventResponse> content = eventPage.getContent().stream()
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
     * @return Evento encontrado o empty si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UIEventResponse> getEventById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse);
    }

    /**
     * Mapea una entidad UIEvent a un DTO UIEventResponse.
     */
    private UIEventResponse mapToResponse(UIEvent event) {
        return new UIEventResponse(
                event.getId(),
                event.getType(),
                event.getAppId(),
                event.getElementId(),
                event.getElementType(),
                event.getRoute(),
                event.getUserId(),
                event.getMetadata(),
                event.getCoordinateX(),
                event.getCoordinateY(),
                event.getScreenWidth(),
                event.getScreenHeight(),
                event.getCreatedAt()
        );
    }
}

