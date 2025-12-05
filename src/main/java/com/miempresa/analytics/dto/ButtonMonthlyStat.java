package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ButtonMonthlyStat {

    @JsonProperty("buttonId")
    private String buttonId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("month")
    @JsonFormat(pattern = "yyyy-MM-01")
    private LocalDate month;

    @JsonProperty("totalClicks")
    private long totalClicks;

    // Constructores
    public ButtonMonthlyStat() {
    }

    public ButtonMonthlyStat(String buttonId, String type, LocalDate month, long totalClicks) {
        this.buttonId = buttonId;
        this.type = type;
        this.month = month;
        this.totalClicks = totalClicks;
    }

    // Getters y Setters
    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }
}

