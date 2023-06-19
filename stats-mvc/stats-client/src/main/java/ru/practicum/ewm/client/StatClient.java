package ru.practicum.ewm.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.HitDTO;
import ru.practicum.ewm.StatAnswerDTO;
import ru.practicum.ewm.StatDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class StatClient {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WebClient client;
    @Value("${client.url:http://localhost:9090}")
    private String url;

    public StatClient() {
        this.client = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public StatAnswerDTO createStat(StatDTO statDTO) {
        return client
                .post()
                .uri("/hit")
                .body(statDTO, StatDTO.class)
                .retrieve()
                .bodyToMono(StatAnswerDTO.class)
                .block();
    }

    public ResponseEntity<List<HitDTO>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startDate = start.format(timeFormatter);
        String endDate = end.format(timeFormatter);
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", startDate)
                        .queryParam("end", endDate)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique.toString())
                        .build())
                .retrieve()
                .toEntityList(HitDTO.class)
                .block();
    }
}

