package ru.practicum.eventRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventRequest.model.EventRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    List<EventRequest> findAllByRequesterId(Long userId);

    Optional<EventRequest> findByIdAndRequesterId(Long requestId, Long userId);

    List<EventRequest> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    List<EventRequest> findAllByEventIdAndEventInitiatorIdAndIdIn(Long eventId, Long userId, Set<Long> eventRequestIds);
}
