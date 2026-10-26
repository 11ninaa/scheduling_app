package com.teamlift.scheduler.controller;

import com.teamlift.scheduler.dto.BookingRequest;
import com.teamlift.scheduler.dto.CreateSlotRequest;
import com.teamlift.scheduler.dto.SlotResponse;
import com.teamlift.scheduler.service.SlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public List<SlotResponse> getAllSlots() {
        return slotService.getAllSlots();
    }

    @GetMapping("/available")
    public List<SlotResponse> getAvailableSlots() {
        return slotService.getAvailableSlots();
    }

    @PostMapping
    public ResponseEntity<?> createSlot(
            @RequestBody CreateSlotRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(slotService.createSlot(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<?> bookSlot(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {
        try {
            return ResponseEntity.ok(
                    slotService.bookSlot(id, request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(exception.getMessage());
        }
    }
}