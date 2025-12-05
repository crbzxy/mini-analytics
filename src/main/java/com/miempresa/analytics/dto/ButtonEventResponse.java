package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ButtonEventResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("appId")
    private String appId;

    @JsonProperty("buttonId")
    private String buttonId;

    @JsonProperty("route")
    private String route;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("metadata")
    private String metadata;

    @JsonProperty("coordinateX")
    private Integer coordinateX;

    @JsonProperty("coordinateY")
    private Integer coordinateY;

    @JsonProperty("screenWidth")
    private Integer screenWidth;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    // Constructores
    public ButtonEventResponse() {
    }

    public ButtonEventResponse(Long id, String type, String appId, String buttonId, 
                              String route, String userId, String metadata, 
                              Integer coordinateX, Integer coordinateY, Integer screenWidth,
                              LocalDateTime createdAt) {
        this.id = id;
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

