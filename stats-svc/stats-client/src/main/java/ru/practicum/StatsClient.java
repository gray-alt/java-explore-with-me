package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.BaseClient;
import ru.practicum.HitDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class StatsClient extends BaseClient {
    public StatsClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> createHit(String app, String uri, String ip) {
        return post("/hit", new HitDto(app, uri, ip, LocalDateTime.now()));
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           @Nullable Set<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get("/stats", null, parameters);
    }
}
