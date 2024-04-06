package rocketseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.CheckIn;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
    Optional<CheckIn> findByAttendeeId(String attendeeId);

}
