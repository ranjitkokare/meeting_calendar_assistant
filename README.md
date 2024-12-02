# Meeting Calendar Assistant
## Overview
The Meeting Calendar Assistant is a Spring Boot application designed to manage employee calendars and help schedule meetings efficiently. The system includes the following functionalities:
1. Book meetings for a calendar owner.
2. Identify free slots for scheduling a meeting of a fixed duration between two employees.
3. Detect scheduling conflicts for meeting participants.

---

## Features
- **Book Meetings:** Users can book meetings for calendar owners with specific participants.
- **Find Free Slots:** Retrieve common free slots between two employees for a specified duration.
- **Conflict Detection:** Identify participants with meeting conflicts for a given meeting request.

---

## Technologies Used
- **Backend Framework:** Spring Boot
- **Database:** MySQL
- **Testing Tool:** Postman, JUnit, Mockito
- **Java 17**
- **Dependency Management:** Maven

---

## Prerequisites
Ensure you have the following installed:
- Java Development Kit (JDK 17+)
- Maven
- MySQL
- Postman (for API testing)

---

## Setup Instructions

### Step 1: Clone the Repository
```bash
git clone https://github.com/<your-repo>/meeting-calendar-assistant.git
cd meeting-calendar-assistant
```

### Step 2: Configure MySQL Database
Create a MySQL database named `meeting_calendar_assistant`. Update the database credentials in the `application.properties` file.

#### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/meeting_calendar_assistant
spring.datasource.username=<your-db-username>
spring.datasource.password=<your-db-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

### Step 3: Install Dependencies
Run the following command to download and install dependencies:
```bash
mvn clean install
```

### Step 4: Run the Application
Start the application using Maven:
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

---

## API Endpoints
### Base URL: `http://localhost:8080/calendar`

### 1. **Book a Meeting**
   - **Endpoint:** `POST /book`
   - **Description:** Book a meeting for the calendar owner.
   - **Request Body Example:**
     ```json
     {
       "ownerEmail": "owner@example.com",
       "startTime": "30-11-2024 10:00 AM",
       "endTime": "30-11-2024 11:00 AM",
       "participants": ["participant1@example.com", "participant2@example.com"]
     }
     ```
   - **Response:** `Meeting successfully booked`

### 2. **Find Free Slots**
   - **Endpoint:** `GET /free-slots`
   - **Description:** Retrieve free time slots between two employees for a specified duration.
   - **Query Parameters:**
     - `employee1` (String): Email of the first employee.
     - `employee2` (String): Email of the second employee.
     - `durationMinutes` (Integer): Duration of the meeting in minutes.
   - **Response Example:**
     ```json
     [
       {
         "start": "30-11-2024 10:00 AM",
         "end": "30-11-2024 10:30 AM"
       }
     ]
     ```

### 3. **Find Participant Conflicts**
   - **Endpoint:** `POST /conflicts`
   - **Description:** Identify participants with meeting conflicts for a given meeting request.
   - **Request Body Example:**
     ```json
     {
       "ownerEmail": "owner@example.com",
       "startTime": "30-11-2024 10:00 AM",
       "endTime": "30-11-2024 11:00 AM",
       "participants": ["participant1@example.com", "participant2@example.com"]
     }
     ```
   - **Response Example:**
     ```json
     {
       "conflictingParticipants": ["participant2@example.com"]
     }
     ```

### Postman Documentation
For detailed API testing, refer to the Postman Collection: [Meeting Calendar Assistant API Documentation](https://documenter.getpostman.com/view/33540913/2sAYBYgAQx).
---
## Testing and TDD Approach

Our project incorporates **JUnit** for comprehensive testing and follows a **Test-Driven Development (TDD)** methodology to ensure code reliability, maintainability, and alignment with project requirements.

### Testing Frameworks and Tools Used
- **JUnit**: For writing and running test cases.
- **Mockito**: For mocking dependencies and simulating behaviors in unit tests.

---
## Project Structure
```plaintext
src/main/java/com/meeting_calendar_assistant
├── controller   # Contains REST controllers for API endpoints
├── dto          # Data Transfer Objects for requests and responses
├── entity       # JPA entities
├── exception    # Custom exceptions and global exception handling
├── repository   # Repository interfaces for database interaction
├── service      # Service layer with business logic
└── util         # Utility classes for reusable functions

```
### Running Tests
To run all test cases:
```bash
mvn clean test
```
---
