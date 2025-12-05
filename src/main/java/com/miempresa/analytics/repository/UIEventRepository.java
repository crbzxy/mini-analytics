package com.miempresa.analytics.repository;

import com.miempresa.analytics.model.UIEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UIEventRepository extends JpaRepository<UIEvent, Long> {

    /**
     * Query nativa para obtener estadísticas mensuales agrupadas por element_id, type y mes.
     * Usa DATE_FORMAT de MySQL para truncar la fecha al mes.
     */
    @Query(value = "SELECT " +
            "be.element_id as elementId, " +
            "be.type as type, " +
            "DATE_FORMAT(be.created_at, '%Y-%m-01') as month, " +
            "COUNT(*) as totalClicks " +
            "FROM ui_events be " +
            "GROUP BY be.element_id, be.type, DATE_FORMAT(be.created_at, '%Y-%m-01') " +
            "ORDER BY month DESC, totalClicks DESC",
            nativeQuery = true)
    List<Object[]> findMonthlyStats();
}

