package com.meeting_calendar_assistant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meeting_calendar_assistant.dto.MeetingDTO;
import com.meeting_calendar_assistant.dto.ParticipantConflictResponse;
import com.meeting_calendar_assistant.dto.SlotDTO;
import com.meeting_calendar_assistant.entity.Calendar;
import com.meeting_calendar_assistant.exception.ConflictException;
import com.meeting_calendar_assistant.exception.ResourceNotFoundException;
import com.meeting_calendar_assistant.repository.CalendarRepository;
import com.meeting_calendar_assistant.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepo;

    @Override
    public void bookMeeting(MeetingDTO meetingDTO) {
    	
    	 // Validate startTime and endTime
        if (meetingDTO.getStartTime().isAfter(meetingDTO.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time. Please check the meeting times.");
        }
        
    	 // Check owner conflicts
        List<Calendar> ownerConflicts = calendarRepo.findOverlappingMeetingsByOwner(
            meetingDTO.getOwnerEmail(), meetingDTO.getStartTime(), meetingDTO.getEndTime()
        );

        if (!ownerConflicts.isEmpty()) {
            throw new ConflictException("Meeting overlaps with an existing meeting for the owner.");
        }

        // Check participant conflicts
        for (String participant : meetingDTO.getParticipants()) {
            List<Calendar> participantConflicts = calendarRepo.findOverlappingMeetingsByParticipant(
                participant, meetingDTO.getStartTime(), meetingDTO.getEndTime()
            );

            if (!participantConflicts.isEmpty()) {
                throw new ConflictException("Meeting overlaps with an existing meeting for " + participant);
            }
        }

        // Save the meeting
        Calendar newMeeting = new Calendar();
        newMeeting.setOwnerEmail(meetingDTO.getOwnerEmail());
        newMeeting.setStartTime(meetingDTO.getStartTime());
        newMeeting.setEndTime(meetingDTO.getEndTime());
        calendarRepo.save(newMeeting);

        // Save participants
        for (String participant : meetingDTO.getParticipants()) {
        	 Calendar newMeeting1 = new Calendar();
             newMeeting1.setOwnerEmail(participant);
             newMeeting1.setStartTime(meetingDTO.getStartTime());
             newMeeting1.setEndTime(meetingDTO.getEndTime());
             calendarRepo.save(newMeeting1);	
        }
    }

    @Override
    public List<SlotDTO> findFreeSlots(String employee1, String employee2, int durationMinutes) {
        // Fetch meetings for both employees
        List<Calendar> emp1Meetings = calendarRepo.findByOwnerEmail(employee1); // Pass employee1
        List<Calendar> emp2Meetings = calendarRepo.findByOwnerEmail(employee2); // Pass employee2

        // Calculate free slots
        List<SlotDTO> freeSlots = TimeUtil.findCommonFreeSlots(emp1Meetings, emp2Meetings, durationMinutes);
        if (freeSlots.isEmpty()) {
            throw new ResourceNotFoundException("No free slots available for the specified duration.");
        }
        return freeSlots;
    }
    
    @Override
    public ParticipantConflictResponse findConflicts(MeetingDTO meetingDTO) {
    	
    	 // Validate startTime and endTime
        if (meetingDTO.getStartTime().isAfter(meetingDTO.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time. Please check the meeting times.");
        }
        
        List<String> conflictingParticipants = new ArrayList<>();
        
        for (String participant : meetingDTO.getParticipants()) {
            List<Calendar> participantConflicts = calendarRepo.findOverlappingMeetingsByParticipant(
                participant, meetingDTO.getStartTime(), meetingDTO.getEndTime()
            );

            if (!participantConflicts.isEmpty()) {
                conflictingParticipants.add(participant);
            }
        }

        ParticipantConflictResponse response = new ParticipantConflictResponse(conflictingParticipants);
        return response;
    }

}