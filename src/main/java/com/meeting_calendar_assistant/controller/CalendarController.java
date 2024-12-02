package com.meeting_calendar_assistant.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meeting_calendar_assistant.dto.MeetingDTO;
import com.meeting_calendar_assistant.dto.ParticipantConflictResponse;
import com.meeting_calendar_assistant.dto.SlotDTO;
import com.meeting_calendar_assistant.service.CalendarService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping("/book")
    public ResponseEntity<String> bookMeeting(@RequestBody @Valid MeetingDTO meetingDTO) {
        calendarService.bookMeeting(meetingDTO);
        return ResponseEntity.ok("Meeting successfully booked");
    }

    @GetMapping("/free-slots")
    public ResponseEntity<List<SlotDTO>> findFreeSlots(
            @RequestParam String employee1,
            @RequestParam String employee2,
            @RequestParam int durationMinutes) {
        return ResponseEntity.ok(calendarService.findFreeSlots(employee1, employee2, durationMinutes));
    }

    @PostMapping("/conflicts")
    public ResponseEntity<ParticipantConflictResponse> findConflicts(@RequestBody MeetingDTO meetingDTO) {
        return ResponseEntity.ok(calendarService.findConflicts(meetingDTO));
    }
}
