package rocketseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rocketseat.com.passin.domain.events.Event;
import rocketseat.com.passin.dto.event.EventAttendees;
import rocketseat.com.passin.dto.event.EventCountAttendees;
import rocketseat.com.passin.dto.event.EventResponseDTO;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String>{

    @Query(value = "SELECT e.id, e.title, e.details, e.slug, e.maximum_attendees as maximumAttendees, COUNT(a.event_id) as attendeesAmount FROM events as e LEFT JOIN attendees as a on a.event_id = e.id WHERE e.id = :id GROUP BY e.id", nativeQuery = true)
    Optional<EventResponseDTO> getEventDetails(String id);

    @Query(value = "SELECT a.*, a.created_at as createdAt, c.created_at as checkedInAt FROM attendees as a INNER JOIN events as e ON e.id = a.event_id LEFT JOIN check_ins as c on a.id = c.attendee_id WHERE e.id = :eventId", nativeQuery = true)
    Optional<List<EventAttendees>> findAttendeesFromEvent(String eventId);

    @Query(value = "SELECT COUNT(a.id) as attendeesAmount FROM events as e LEFT JOIN attendees as a on e.id = a.event_id WHERE e.id = :eventId GROUP BY e.id", nativeQuery = true)
    EventCountAttendees findAttendeesAmountFromEvent(String eventId);
}