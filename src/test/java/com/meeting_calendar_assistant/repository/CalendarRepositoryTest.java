package com.meeting_calendar_assistant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.meeting_calendar_assistant.entity.Calendar;

@DataJpaTest
public class CalendarRepositoryTest {

    @Autowired
    private CalendarRepository calendarRepo;

    @Test
    public void testFindOverlappingMeetingsByOwner_ShouldReturnConflicts() {
        Calendar meeting = new Calendar();
        meeting.setOwnerEmail("owner@example.com");
        meeting.setStartTime(LocalDateTime.of(2024, 11, 30, 10, 0));
        meeting.setEndTime(LocalDateTime.of(2024, 11, 30, 11, 0));
        calendarRepo.save(meeting);

        List<Calendar> conflicts = calendarRepo.findOverlappingMeetingsByOwner(
            "owner@example.com", 
            LocalDateTime.of(2024, 11, 30, 10, 30), 
            LocalDateTime.of(2024, 11, 30, 11, 30)
        );

        Assertions.assertFalse(conflicts.isEmpty());
    }

    @Test
    public void testFindOverlappingMeetingsByOwner_NoConflicts_ShouldReturnEmpty() {
        Calendar meeting = new Calendar();
        meeting.setOwnerEmail("owner@example.com");
        meeting.setStartTime(LocalDateTime.of(2024, 11, 30, 10, 0));
        meeting.setEndTime(LocalDateTime.of(2024, 11, 30, 11, 0));
        calendarRepo.save(meeting);

        List<Calendar> conflicts = calendarRepo.findOverlappingMeetingsByOwner(
            "owner@example.com", 
            LocalDateTime.of(2024, 11, 30, 12, 0), 
            LocalDateTime.of(2024, 11, 30, 13, 0)
        );

        Assertions.assertTrue(conflicts.isEmpty());
    }
}

