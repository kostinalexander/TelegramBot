package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.DogShController;
import com.example.shelterBot.model.shelter.DogShelter;
import com.example.shelterBot.service.DogShService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.only;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(DogShController.class)
public class ControllerDogsShelterTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogShService service;

    @Autowired
    private ObjectMapper mapper;

    static DogShelter test = new DogShelter("nameSh","zar", LocalTime.NOON);

    List<DogShelter> list = new ArrayList<>(List.of(new DogShelter("nameSh1", "zag", LocalTime.NOON)
            , new DogShelter("nameSh2", "ger", LocalTime.NOON)
            , new DogShelter("nameSh3", "rar", LocalTime.NOON)));

    @Test
    @DisplayName("Добавление dogSh")
    void createDogSh() throws Exception {

        when(service.addDogSh(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.post("/dog_shelter")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameShelter").value("nameSh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("zar"))
                .andExpect(status().isOk());
        verify(service, only()).addDogSh(test);
    }

    @Test
    @DisplayName("All dogSh")
    void getAllDogsSh() throws Exception {
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/dog_shelter")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).getAll();
    }

    @Test
    @DisplayName("Поиск dogSh по id")
    void findById() throws Exception {
        when(service.findShelterForDogs(anyLong())).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.get("/dog_shelter/{id}", test.getId()))
                .andExpect(status().isOk());

        verify(service, only()).findShelterForDogs(test.getId());
    }

    @Test
    @DisplayName("Изменение dogSh")
    void editDogSh() throws Exception {
        when(service.editDogShelter(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.put("/dog_shelter")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameShelter").value("nameSh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("zar"))
                .andExpect(status().isOk());

        verify(service, only()).editDogShelter(test);
    }

    @Test
    @DisplayName("Удаление DogShelter")
    void deleteCat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog_shelter/0")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
