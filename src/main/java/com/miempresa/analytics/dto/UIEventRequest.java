package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UIEventRequest {

    @JsonProperty("type")
    @NotBlank(message = "type is required")
    @Size(max = 50, message = "type must not exceed 50 characters")
    private String type;

    @JsonProperty("appId")
    private String appId;

    @JsonProperty("elementId")
    @NotBlank(message = "elementId is required")
    @Size(max = 100, message = "elementId must not exceed 100 characters")
    private String elementId;

    @JsonProperty("elementType")
    @Size(max = 50, message = "elementType must not exceed 50 characters")
    private String elementType;

    @JsonProperty("route")
    private String route;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("metadataJson")
    private String metadataJson;

    @JsonProperty("coordinateX")
    private Integer coordinateX;

    @JsonProperty("coordinateY")
    private Integer coordinateY;

    @JsonProperty("screenWidth")
    private Integer screenWidth;

    @JsonProperty("screenHeight")
    private Integer screenHeight;

    @JsonProperty("createdAt")
    private String createdAt;

    // Constructores
    public UIEventRequest() {
    }

    // Getters y Setters
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

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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

