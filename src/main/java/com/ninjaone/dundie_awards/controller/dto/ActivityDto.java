package com.ninjaone.dundie_awards.controller.dto;

import java.time.LocalDateTime;

public class ActivityDto {
    private Long id;
    private LocalDateTime occuredAt;
    private String event;


    public LocalDateTime getOccuredAt() {
        return occuredAt;
    }

    public void setOccuredAt(LocalDateTime occuredAt) {
        this.occuredAt = occuredAt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

