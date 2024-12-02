package com.meeting_calendar_assistant.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantConflictResponse {
	private List<String> conflictingParticipants; // List of participants with overlapping meetings
}
