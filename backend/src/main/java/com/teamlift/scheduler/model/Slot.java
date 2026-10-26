package com.teamlift.scheduler.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate slotDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private boolean booked = false;

    private String bookedByName;

    private String bookedByEmail;

    @Column(columnDefinition = "TEXT")
    private String aiConfirmationNote;

    @Version
    private Long version;

    public Slot() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getBookedByName() {
        return bookedByName;
    }

    public void setBookedByName(String bookedByName) {
        this.bookedByName = bookedByName;
    }

    public String getBookedByEmail() {
        return bookedByEmail;
    }

    public void setBookedByEmail(String bookedByEmail) {
        this.bookedByEmail = bookedByEmail;
    }

    public String getAiConfirmationNote() {
        return aiConfirmationNote;
    }

    public void setAiConfirmationNote(String aiConfirmationNote) {
        this.aiConfirmationNote = aiConfirmationNote;
    }
}