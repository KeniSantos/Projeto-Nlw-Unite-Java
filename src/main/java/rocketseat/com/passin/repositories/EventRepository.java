package rocketseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passin.domain.events.Event;

public interface EventRepository extends JpaRepository<Event, String>{

}