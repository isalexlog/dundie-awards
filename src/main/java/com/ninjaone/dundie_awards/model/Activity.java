package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "occured_at")
    private LocalDateTime occuredAt;

    @Column(name = "event")
    private String event;

    @Column(name = "processed")
    private boolean processed;

    public Activity() {
    }

    public Activity(LocalDateTime localDateTime, String event) {
        super();
        this.occuredAt = localDateTime;
        this.event = event;
    }

    public LocalDateTime getOccuredAt() {
        return occuredAt;
    }

    public String getEvent() {
        return event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOccuredAt(LocalDateTime occuredAt) {
        this.occuredAt = occuredAt;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", occuredAt=" + occuredAt +
                ", event='" + event + '\'' +
                ", processed=" + processed +
                '}';
    }
}
