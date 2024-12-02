package com.meeting_calendar_assistant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meeting_calendar_assistant.entity.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
	@Query("SELECT c FROM Calendar c WHERE c.ownerEmail = :ownerEmail AND c.startTime < :endTime AND c.endTime > :startTime")
	List<Calendar> findOverlappingMeetingsByOwner(@Param("ownerEmail") String ownerEmail, 
	                                              @Param("startTime") LocalDateTime startTime, 
	                                              @Param("endTime") LocalDateTime endTime);


	@Query("SELECT c FROM Calendar c WHERE c.ownerEmail = :participantEmail AND c.startTime < :endTime AND c.endTime > :startTime")
		List<Calendar> findOverlappingMeetingsByParticipant(@Param("participantEmail") String participantEmail, 
		                                                     @Param("startTime") LocalDateTime startTime, 
		                                                     @Param("endTime") LocalDateTime endTime);

		
	@Query("SELECT c FROM Calendar c WHERE c.ownerEmail = :email")
	List<Calendar> findByOwnerEmail(@Param("email") String email);
    
//    @Query("SELECT c FROM Calendar c JOIN MeetingParticipants p ON c.id = p.meetingId WHERE p.participantEmail = :participantEmail")
//    List<Calendar> findMeetingsByParticipantEmail(@Param("participantEmail") String participantEmail);
}

