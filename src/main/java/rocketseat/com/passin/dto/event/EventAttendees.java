package rocketseat.com.passin.dto.event;

import java.time.LocalDateTime;

public interface EventAttendees {
    String getId();
    String getName();
    String getEmail();
    LocalDateTime getCreatedAt();
    LocalDateTime getCheckedInAt();
}
