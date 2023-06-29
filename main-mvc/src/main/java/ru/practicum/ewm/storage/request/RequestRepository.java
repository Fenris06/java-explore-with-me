package ru.practicum.ewm.storage.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUser_Id(Long id);

    Optional<Request> findByUser_IdAndId(Long id, Long requestId);
    List<Request> findByEvent_User_IdAndEvent_Id(Long id, Long eventId);

    List<Request> findByEvent_Id(Long id);

    List<Request> findByUser_IdAndEvent_Id(Long id, Long id1);
}
