package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ButtonEventRequest {

    @JsonProperty("type")
    @NotBlank(message = "type is required")
    @Size(max = 50, message = "type must not exceed 50 characters")
    private String type;

    @JsonProperty("appId")
    private String appId;

    @JsonProperty("buttonId")
    @NotBlank(message = "buttonId is required")
    @Size(max = 100, message = "buttonId must not exceed 100 characters")
    private String buttonId;

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

    @JsonProperty("createdAt")
    private String createdAt;

    // Constructores
    public ButtonEventRequest() {
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
}

