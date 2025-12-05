package com.miempresa.analytics.repository;

import com.miempresa.analytics.dto.ButtonMonthlyStat;
import com.miempresa.analytics.model.ButtonEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ButtonEventRepository extends JpaRepository<ButtonEvent, Long> {

    /**
     * Query nativa para obtener estadísticas mensuales agrupadas por button_id, type y mes.
     * Usa DATE_FORMAT de MySQL para truncar la fecha al mes.
     */
    @Query(value = "SELECT " +
            "button_id as buttonId, " +
            "type, " +
            "DATE_FORMAT(created_at, '%Y-%m-01') as month, " +
            "COUNT(*) as totalClicks " +
            "FROM button_events " +
            "GROUP BY button_id, type, DATE_FORMAT(created_at, '%Y-%m-01') " +
            "ORDER BY month DESC, button_id, type",
            nativeQuery = true)
    List<Object[]> findMonthlyStatsNative();

    /**
     * Query alternativa que retorna directamente ButtonMonthlyStat usando proyección.
     * Se mapea manualmente en el servicio.
     */
    @Query(value = "SELECT " +
            "button_id, " +
            "type, " +
            "DATE_FORMAT(created_at, '%Y-%m-01') as month, " +
            "COUNT(*) as totalClicks " +
            "FROM button_events " +
            "GROUP BY button_id, type, DATE_FORMAT(created_at, '%Y-%m-01') " +
            "ORDER BY month DESC, button_id, type",
            nativeQuery = true)
    List<Object[]> findMonthlyStats();
}

