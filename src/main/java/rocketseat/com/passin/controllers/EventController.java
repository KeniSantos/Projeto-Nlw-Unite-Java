package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.event.EventDetailDTO;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventListAttendees;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailDTO> getEvent(@PathVariable String id){
        EventDetailDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<EventListAttendees> getEventAttendees(@PathVariable String id){
        EventListAttendees eventListAttendees = this.eventService.getEventsAttendee(id);
        return ResponseEntity.ok(eventListAttendees);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO event = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.id()).toUri();

        return ResponseEntity.created(uri).body(event);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId,
                                                          @RequestBody AttendeeRequestDTO body,
                                                          UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.id()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

}
