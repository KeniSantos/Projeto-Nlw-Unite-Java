package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.events.exceptions.EventFullException;
import rocketseat.com.passin.domain.events.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.event.*;
import rocketseat.com.passin.repositories.EventRepository;
import rocketseat.com.passin.domain.events.Event;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventListAttendees getEventsAttendee(String eventId){
        List<EventAttendees> eventAttendees = this.eventRepository.findAttendeesFromEvent(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        return new EventListAttendees(eventAttendees);
    }

    public EventDetailDTO getEventDetail(String eventId) {
        EventResponseDTO event =  this.eventRepository.getEventDetails(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        return new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), event.getAttendeesAmount());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDto){
        Event newEvent = new Event();

        newEvent.setTitle(eventDto.title());
        newEvent.setDetails(eventDto.details());
        newEvent.setMaximumAttendees(eventDto.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDto.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);
        Event event = this.getEventById(eventId);
        EventCountAttendees attendeeList = this.eventRepository.findAttendeesAmountFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.getAttendeesAmount()) throw new EventFullException("Event is full");

        System.out.println(attendeeList.getAttendeesAmount());

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }

    private String createSlug(String text) {
        String normalizer = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizer.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

}
