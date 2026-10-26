package com.teamlift.scheduler.dto;

import com.teamlift.scheduler.model.Slot;
import java.time.LocalDate;
import java.time.LocalTime;

public class SlotResponse {

    private Long id;
    private LocalDate slotDate;
    private LocalTime startTime;
    private Integer durationMinutes;
    private String topic;
    private boolean booked;
    private String bookedByName;
    private String aiConfirmationNote;

    public SlotResponse() {
    }

    public static SlotResponse fromEntity(Slot slot) {
        SlotResponse response = new SlotResponse();

        response.setId(slot.getId());
        response.setSlotDate(slot.getSlotDate());
        response.setStartTime(slot.getStartTime());
        response.setDurationMinutes(slot.getDurationMinutes());
        response.setTopic(slot.getTopic());
        response.setBooked(slot.isBooked());
        response.setBookedByName(slot.getBookedByName());
        response.setAiConfirmationNote(slot.getAiConfirmationNote());

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAiConfirmationNote() {
        return aiConfirmationNote;
    }

    public void setAiConfirmationNote(String aiConfirmationNote) {
        this.aiConfirmationNote = aiConfirmationNote;
    }
}