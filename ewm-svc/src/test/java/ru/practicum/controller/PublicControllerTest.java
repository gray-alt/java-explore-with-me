package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.StatsClient;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.service.EventService;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PublicControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final CategoryService categoryService;
    @MockBean
    private final EventService eventService;
    @MockBean
    private final CompilationService compilationService;
    @MockBean
    private final StatsClient statsClient;
    @MockBean
    private final LocationService locationService;

    private final List<Location> locations = Collections.singletonList(Location.builder()
            .id(1L)
            .name("Test location")
            .lat(15.50D)
            .lon(15.50D)
            .radius(50D)
            .build());

    @Test
    void getLocations() throws Exception {
        when(locationService.getAllLocations(anyInt(), anyInt()))
                .thenReturn(locations);

        mockMvc.perform(get("/locations")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(locations.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].lat", is(locations.get(0).getLat()), Double.class))
                .andExpect(jsonPath("$[0].lon", is(locations.get(0).getLon()), Double.class))
                .andExpect(jsonPath("$[0].radius", is(locations.get(0).getRadius()), Double.class));
    }

    @Test
    void getLocationById() throws Exception {
        when(locationService.getLocationById(anyLong()))
                .thenReturn(locations.get(0));

        mockMvc.perform(get("/locations/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(locations.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.lat", is(locations.get(0).getLat()), Double.class))
                .andExpect(jsonPath("$.lon", is(locations.get(0).getLon()), Double.class))
                .andExpect(jsonPath("$.radius", is(locations.get(0).getRadius()), Double.class));
    }
}
