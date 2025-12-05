package com.miempresa.analytics.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "button_events")
public class ButtonEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "app_id", length = 100)
    private String appId;

    @Column(name = "button_id", nullable = false, length = 100)
    private String buttonId;

    @Column(name = "route", length = 255)
    private String route;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "coordinate_x")
    private Integer coordinateX;

    @Column(name = "coordinate_y")
    private Integer coordinateY;

    @Column(name = "screen_width")
    private Integer screenWidth;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructores
    public ButtonEvent() {
    }

    public ButtonEvent(String type, String appId, String buttonId, String route, 
                      String userId, String metadata, Integer coordinateX, 
                      Integer coordinateY, Integer screenWidth, LocalDateTime createdAt) {
        this.type = type;
        this.appId = appId;
        this.buttonId = buttonId;
        this.route = route;
        this.userId = userId;
        this.metadata = metadata;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.screenWidth = screenWidth;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(Integer coordinateX) {
        this.coordinateX = coordinateX;
    }

    public Integer getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(Integer coordinateY) {
        this.coordinateY = coordinateY;
    }

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }
}

