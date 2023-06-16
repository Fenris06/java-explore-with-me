package ru.practicum.ewm.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.Stat;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase
public class StatRepositoryTest {
    final LocalDateTime start = LocalDateTime.of(2023, 5, 1, 1, 0, 0);
    final LocalDateTime end = LocalDateTime.of(2023, 6, 29, 0, 0, 0);
    final List<String> events = List.of("/events/1", "/events/2");
    final List<String> event1 = List.of("/events/1");
    final LocalDateTime startEmpty = LocalDateTime.of(2023, 4, 1, 1, 1, 1);
    final LocalDateTime endEmpty = LocalDateTime.of(2023, 5, 1, 0, 59, 59);
    Stat first;
    Stat second;
    Stat third;
    Stat fourth;
    Stat five;
    Stat six;
    @Autowired
    StatRepository statRepository;

    @BeforeEach
    void beforeEach() {
        first = new Stat();
        first.setApp("ewm-main-service");
        first.setUri("/events/1");
        first.setIp("192.163.0.1");
        first.setTimestamp(LocalDateTime.of(2023, 5, 6, 11, 0, 23));
        statRepository.save(first);

        second = new Stat();
        second.setApp("ewm-main-service");
        second.setUri("/events/1");
        second.setIp("192.163.0.1");
        second.setTimestamp(LocalDateTime.of(2023, 6, 7, 11, 0, 23));
        statRepository.save(second);

        third = new Stat();
        third.setApp("ewm-main-service");
        third.setUri("/events/1");
        third.setIp("192.163.0.3");
        third.setTimestamp(LocalDateTime.of(2023, 5, 7, 11, 0, 23));
        statRepository.save(third);

        fourth = new Stat();
        fourth.setApp("ewm-main-service");
        fourth.setUri("/events/2");
        fourth.setIp("192.163.0.1");
        fourth.setTimestamp(LocalDateTime.of(2023, 5, 8, 11, 0, 23));
        statRepository.save(fourth);

        five = new Stat();
        five.setApp("ewm-main-service");
        five.setUri("/events/2");
        five.setIp("192.163.0.1");
        five.setTimestamp(LocalDateTime.of(2023, 5, 11, 11, 0, 23));
        statRepository.save(five);

        six = new Stat();
        six.setApp("ewm-main-service");
        six.setUri("/events/2");
        six.setIp("192.163.0.2");
        six.setTimestamp(LocalDateTime.of(2023, 5, 7, 11, 0, 23));
        statRepository.save(six);
    }

    @AfterEach
    void afterEach() {
        statRepository.deleteAll();
    }

    @Test
    void should_GetStartReturnListHiT_IfStartEndEventsIsExist() {
        List<Hit> hits = statRepository.getStat(start, end, events);
        assertEquals(hits.size(), 2);
        assertEquals(hits.get(0).getApp(), "ewm-main-service");
        assertEquals(hits.get(1).getApp(), "ewm-main-service");
        assertEquals(hits.get(0).getUri(), "/events/1");
        assertEquals(hits.get(1).getUri(), "/events/2");
        assertEquals(hits.get(0).getHits(), 3);
        assertEquals(hits.get(1).getHits(), 3);
    }

    @Test
    void should_GetStartDistinctReturnListHiT_IfStartEndEventsIsExist() {
        List<Hit> hits = statRepository.getStatDistinct(start, end, events);
        assertEquals(hits.size(), 2);
        assertEquals(hits.get(0).getApp(), "ewm-main-service");
        assertEquals(hits.get(1).getApp(), "ewm-main-service");
        assertEquals(hits.get(0).getUri(), "/events/1");
        assertEquals(hits.get(1).getUri(), "/events/2");
        assertEquals(hits.get(0).getHits(), 2);
        assertEquals(hits.get(1).getHits(), 2);
    }

    @Test
    void should_GetStartReturnEmptyListHiT_IfStartEndIsBefore() {
        List<Hit> hits = statRepository.getStat(startEmpty, endEmpty, events);
        assertEquals(hits.size(), 0);
    }

    @Test
    void should_GetStartDistinctReturnEmptyListHiT_IfStartEndIsBefore() {
        List<Hit> hits = statRepository.getStatDistinct(startEmpty, endEmpty, events);
        assertEquals(hits.size(), 0);
    }

    @Test
    void should_GetStartReturnListHiTOneEvent_IfStartEndEventsIsExist() {
        List<Hit> hits = statRepository.getStat(start, end, event1);
        assertEquals(hits.size(), 1);
        assertEquals(hits.get(0).getApp(), "ewm-main-service");
        assertEquals(hits.get(0).getUri(), "/events/1");
        assertEquals(hits.get(0).getHits(), 3);
    }

    @Test
    void should_GetStartDistinctReturnListHiTOneEvent_IfStartEndEventsIsExist() {
        List<Hit> hits = statRepository.getStatDistinct(start, end, event1);
        assertEquals(hits.size(), 1);
        assertEquals(hits.get(0).getApp(), "ewm-main-service");
        assertEquals(hits.get(0).getUri(), "/events/1");
        assertEquals(hits.get(0).getHits(), 2);
    }
}
