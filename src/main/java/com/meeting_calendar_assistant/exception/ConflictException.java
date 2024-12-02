package com.meeting_calendar_assistant.exception;

/**
 * Exception thrown when a conflict occurs, such as overlapping meetings.
 */

public class ConflictException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new ConflictException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConflictException(String message) {
        super(message);
    }
}
