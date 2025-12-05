package com.miempresa.analytics.service;

import com.miempresa.analytics.dto.UIEventRequest;
import com.miempresa.analytics.dto.UIEventResponse;
import com.miempresa.analytics.dto.UIMonthlyStat;
import com.miempresa.analytics.dto.PageResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio para gestión de eventos de UI.
 * Define los contratos de negocio para operaciones con eventos.
 */
public interface UIEventService {

    /**
     * Valida y guarda un evento de elemento UI.
     * 
     * @param request DTO con los datos del evento
     * @throws IllegalArgumentException si type o elementId están vacíos
     */
    void save(UIEventRequest request);

    /**
     * Obtiene estadísticas mensuales agrupadas por element_id, type y mes.
     * 
     * @return Lista de estadísticas mensuales
     */
    List<UIMonthlyStat> getMonthlyStats();

    /**
     * Obtiene todos los eventos con paginado.
     * 
     * @param page Número de página (0-indexed)
     * @param size Tamaño de la página
     * @return Respuesta paginada con eventos
     */
    PageResponse<UIEventResponse> getAllEvents(int page, int size);

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento
     * @return Evento encontrado o empty si no existe
     */
    Optional<UIEventResponse> getEventById(Long id);
}
