package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.service.EventService;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final LocationService locationService;
    @MockBean
    private final CategoryService categoryService;
    @MockBean
    private final UserService userService;
    @MockBean
    private final EventService eventService;
    @MockBean
    private final CompilationService compilationService;

    private final Location location = Location.builder()
            .id(1L)
            .name("Test location")
            .lat(15.50D)
            .lon(15.50D)
            .radius(50D)
            .build();

    @Test
    void addLocation() throws Exception {
        when(locationService.addLocation(any()))
                .thenReturn(location);

        mockMvc.perform(post("/admin/locations")
                    .content(objectMapper.writeValueAsString(location))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(location.getId()), Long.class))
                .andExpect(jsonPath("$.lat", is(location.getLat()), Double.class))
                .andExpect(jsonPath("$.lon", is(location.getLon()), Double.class))
                .andExpect(jsonPath("$.radius", is(location.getRadius()), Double.class));
    }

    @Test
    void updateLocation() throws Exception {
        when(locationService.updateLocation(any(), any()))
                .thenReturn(location);

        mockMvc.perform(patch("/admin/locations/1")
                        .content(objectMapper.writeValueAsString(location))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(location.getId()), Long.class))
                .andExpect(jsonPath("$.lat", is(location.getLat()), Double.class))
                .andExpect(jsonPath("$.lon", is(location.getLon()), Double.class))
                .andExpect(jsonPath("$.radius", is(location.getRadius()), Double.class));
    }
}
