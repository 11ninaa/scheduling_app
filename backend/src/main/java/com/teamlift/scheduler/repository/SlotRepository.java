package com.teamlift.scheduler.repository;

import com.teamlift.scheduler.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByBookedFalseOrderBySlotDateAscStartTimeAsc();

    List<Slot> findAllByOrderBySlotDateAscStartTimeAsc();
}