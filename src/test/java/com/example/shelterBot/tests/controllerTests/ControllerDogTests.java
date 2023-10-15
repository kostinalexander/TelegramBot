package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.DogController;
import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.service.DogService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.only;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogController.class)
public class ControllerDogTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogService service;

    @Autowired
    private ObjectMapper mapper;

    static Dog dogTest = new Dog("Luk", 2, "Овчарка");

    List<Dog> list = new ArrayList<>(List.of(new Dog("Cup", 2, "Бродячая")
            , new Dog("Zuz", 1, "Парская")
            , new Dog("Dak", 3, "flld")));

    @Test
    @DisplayName("Добавление dog")
    void createDog() throws Exception {

        when(service.addDog(dogTest)).thenReturn(dogTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/dogs")
                        .content(mapper.writeValueAsBytes(dogTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Luk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("Овчарка"))
                .andExpect(status().isOk());
        verify(service, only()).addDog(dogTest);
    }

    @Test
    @DisplayName("All dogs")
    void getAllDogs() throws Exception {
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/dogs")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).getAll();
    }

    @Test
    @DisplayName("Поиск dog по id")
    void findById() throws Exception {
        when(service.findDog(anyLong())).thenReturn(dogTest);

        mockMvc.perform(MockMvcRequestBuilders.get("/dogs/{id}", dogTest.getId()))
                .andExpect(status().isOk());

        verify(service, only()).findDog(dogTest.getId());
    }

    @Test
    @DisplayName("Изменение dog")
    void editDog() throws Exception {
        when(service.editDog(dogTest)).thenReturn(dogTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/dogs")
                        .content(mapper.writeValueAsBytes(dogTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Luk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("Овчарка"))
                .andExpect(status().isOk());

        verify(service, only()).editDog(dogTest);
    }

    @Test
    @DisplayName("Удаление Dog")
    void deleteDog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dogs/0")
                        .content(mapper.writeValueAsBytes(dogTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
