package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UIEventResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("appId")
    private String appId;

    @JsonProperty("elementId")
    private String elementId;

    @JsonProperty("elementType")
    private String elementType;

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

    @JsonProperty("screenHeight")
    private Integer screenHeight;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructores
    public UIEventResponse() {
    }

    public UIEventResponse(Long id, String type, String appId, String elementId, 
                              String elementType, String route, String userId, String metadata, 
                              Integer coordinateX, Integer coordinateY, 
                              Integer screenWidth, Integer screenHeight,
                              LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.appId = appId;
        this.elementId = elementId;
        this.elementType = elementType;
        this.route = route;
        this.userId = userId;
        this.metadata = metadata;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
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

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
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

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }
}

