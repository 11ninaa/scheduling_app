
package com.teamlift.scheduler.service;

import com.teamlift.scheduler.dto.CreateSlotRequest;
import com.teamlift.scheduler.dto.SlotResponse;
import com.teamlift.scheduler.dto.BookingRequest;
import com.teamlift.scheduler.model.Slot;
import com.teamlift.scheduler.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final GeminiAiService geminiAiService;

    private static final Set<Integer> ALLOWED_DURATIONS =
            Set.of(15, 30, 45, 60);

    public SlotService(
            SlotRepository slotRepository,
            GeminiAiService geminiAiService) {
        this.slotRepository = slotRepository;
        this.geminiAiService = geminiAiService;
    }

    public List<SlotResponse> getAllSlots() {
        return slotRepository.findAllByOrderBySlotDateAscStartTimeAsc()
                .stream()
                .map(SlotResponse::fromEntity)
                .toList();
    }

    public List<SlotResponse> getAvailableSlots() {
        return slotRepository.findByBookedFalseOrderBySlotDateAscStartTimeAsc()
                .stream()
                .map(SlotResponse::fromEntity)
                .toList();
    }

    public SlotResponse createSlot(CreateSlotRequest request) {
        if (request.getSlotDate() == null
                || request.getStartTime() == null) {
            throw new IllegalArgumentException(
                    "Date and start time are required.");
        }

        LocalDateTime slotDateTime = LocalDateTime.of(
                request.getSlotDate(),
                request.getStartTime()
        );

        if (!slotDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "The session must be scheduled for a future date and time.");
        }

        if (request.getDurationMinutes() == null
                || !ALLOWED_DURATIONS.contains(
                request.getDurationMinutes())) {
            throw new IllegalArgumentException(
                    "Duration must be 15, 30, 45, or 60 minutes.");
        }

        if (request.getTopic() == null
                || request.getTopic().isBlank()) {
            throw new IllegalArgumentException("Topic is required.");
        }

        Slot slot = new Slot();
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setDurationMinutes(request.getDurationMinutes());
        slot.setTopic(request.getTopic().trim());
        slot.setBooked(false);

        return SlotResponse.fromEntity(slotRepository.save(slot));
    }

    @Transactional
    public SlotResponse bookSlot(Long id, BookingRequest request) {
        if (request.getName() == null
                || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (request.getEmail() == null
                || !request.getEmail().trim().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException(
                    "A valid email is required.");
        }

        Slot slot = slotRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Slot not found."));

        if (slot.isBooked()) {
            throw new IllegalStateException(
                    "This slot has already been booked.");
        }

        slot.setBooked(true);
        slot.setBookedByName(request.getName().trim());
        slot.setBookedByEmail(request.getEmail().trim());

        String note = geminiAiService.generateConfirmation(
                slot.getBookedByName(),
                slot.getTopic()
        );

        slot.setAiConfirmationNote(note);

        Slot savedSlot = slotRepository.save(slot);
        return SlotResponse.fromEntity(savedSlot);
    }
}
