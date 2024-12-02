package com.meeting_calendar_assistant.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.meeting_calendar_assistant.dto.SlotDTO;
import com.meeting_calendar_assistant.entity.Calendar;

public class TimeUtil {
    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1,
                                        LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public static List<SlotDTO> findCommonFreeSlots(List<Calendar> emp1Meetings,
                                                    List<Calendar> emp2Meetings,
                                                    int durationMinutes) {

    	 // Group meetings by date
        Map<LocalDate, List<Calendar>> emp1MeetingsByDate = emp1Meetings.stream()
                .collect(Collectors.groupingBy(meeting -> meeting.getStartTime().toLocalDate()));
        Map<LocalDate, List<Calendar>> emp2MeetingsByDate = emp2Meetings.stream()
                .collect(Collectors.groupingBy(meeting -> meeting.getStartTime().toLocalDate()));

        List<SlotDTO> commonSlots = new ArrayList<>();

        // Process each date
        for (LocalDate date : emp1MeetingsByDate.keySet()) {
            LocalDateTime workStart = date.atTime(9, 0);
            LocalDateTime workEnd = date.atTime(17, 0);
            
            List<SlotDTO> emp1FreeSlots = calculateFreeSlots(emp1MeetingsByDate.get(date), workStart, workEnd, durationMinutes);
            List<SlotDTO> emp2FreeSlots = calculateFreeSlots(emp2MeetingsByDate.getOrDefault(date, List.of()), workStart, workEnd, durationMinutes);

            // Find intersections
            commonSlots.addAll(findSlotIntersections(emp1FreeSlots, emp2FreeSlots));
        }
    			
        return commonSlots;
    }

    private static List<SlotDTO> calculateFreeSlots(List<Calendar> meetings,
                                                    LocalDateTime workStart,
                                                    LocalDateTime workEnd,
                                                    int durationMinutes) {
        List<SlotDTO> freeSlots = new ArrayList<>();
        
     // Sort meetings by start time
        meetings.sort((m1, m2) -> m1.getStartTime().compareTo(m2.getStartTime()));
        
        LocalDateTime current = workStart;

        for (Calendar meeting : meetings) {
            if (meeting.getStartTime().toLocalDate().equals(current.toLocalDate())) {
                if (current.isBefore(meeting.getStartTime())) {
                    // Check if the slot is valid for the duration
                    if (current.plusMinutes(durationMinutes).isBefore(meeting.getStartTime())) {
                        freeSlots.add(new SlotDTO(current, meeting.getStartTime()));
                    }
                }
                current = meeting.getEndTime();
            }
        }

        // Add a slot from the end of the last meeting to the end of the workday
        if (current.isBefore(workEnd)) {
            freeSlots.add(new SlotDTO(current, workEnd));
        }

        return freeSlots;
    }

    private static List<SlotDTO> findSlotIntersections(List<SlotDTO> slots1, List<SlotDTO> slots2) {
        List<SlotDTO> intersections = new ArrayList<>();
        for (SlotDTO slot1 : slots1) {
            for (SlotDTO slot2 : slots2) {
                LocalDateTime start = slot1.getStart().isAfter(slot2.getStart()) ? slot1.getStart() : slot2.getStart();
                LocalDateTime end = slot1.getEnd().isBefore(slot2.getEnd()) ? slot1.getEnd() : slot2.getEnd();
                if (start.plusMinutes(30).isBefore(end)) {
                    intersections.add(new SlotDTO(start, end));
                }
            }
        }
        return intersections;
    }
}
