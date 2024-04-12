package rocketseat.com.passin.dto.event;

public interface EventResponseDTO {
    String getId();
    String getTitle();
    String getDetails();
    String getSlug();
    Integer getMaximumAttendees();
    Integer getAttendeesAmount();
}
