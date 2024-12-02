package com.meeting_calendar_assistant.service;

import java.util.List;

import com.meeting_calendar_assistant.dto.MeetingDTO;
import com.meeting_calendar_assistant.dto.ParticipantConflictResponse;
import com.meeting_calendar_assistant.dto.SlotDTO;

public interface CalendarService {
	void bookMeeting(MeetingDTO meetingDTO);

    List<SlotDTO> findFreeSlots(String employee1, String employee2, int durationMinutes);

    ParticipantConflictResponse findConflicts(MeetingDTO meetingDTO);
}
