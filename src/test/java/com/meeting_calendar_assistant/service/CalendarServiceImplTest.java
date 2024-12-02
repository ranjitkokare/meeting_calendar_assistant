package com.meeting_calendar_assistant.service;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.meeting_calendar_assistant.dto.MeetingDTO;
import com.meeting_calendar_assistant.repository.CalendarRepository;

@SpringBootTest
public class CalendarServiceImplTest {

    @Autowired
    private CalendarServiceImpl calendarService;

    @MockitoBean
    private CalendarRepository calendarRepo;

    @Test
    public void testBookMeeting_WithValidTimes_ShouldSucceed() {
        MeetingDTO meetingDTO = new MeetingDTO("owner@example.com", 
            LocalDateTime.of(2024, 11, 30, 10, 0), 
            LocalDateTime.of(2024, 11, 30, 11, 0),
            List.of("participant1@example.com", "participant2@example.com"));

        Mockito.when(calendarRepo.findOverlappingMeetingsByOwner(any(), any(), any())).thenReturn(List.of());
        Mockito.when(calendarRepo.findOverlappingMeetingsByParticipant(any(), any(), any())).thenReturn(List.of());

        Assertions.assertDoesNotThrow(() -> calendarService.bookMeeting(meetingDTO));
    }

    @Test
    public void testBookMeeting_WithEndTimeBeforeStartTime_ShouldThrowException() {
        MeetingDTO meetingDTO = new MeetingDTO("owner@example.com", 
            LocalDateTime.of(2024, 11, 30, 11, 0), 
            LocalDateTime.of(2024, 11, 30, 10, 0),
            List.of("participant1@example.com", "participant2@example.com"));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, 
            () -> calendarService.bookMeeting(meetingDTO));

        Assertions.assertEquals("Start time cannot be after end time. Please check the meeting times.", exception.getMessage());
    }
}

