package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.CatController;
import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.service.CatService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatController.class)
public class ControllerCatTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService service;

    @Autowired
    private ObjectMapper mapper;

    static Cat catTest = new Cat("Luiza", 1, "Еркширская");

    List<Cat> list = new ArrayList<>(List.of(new Cat("Luz", 2, "Пермская")
            , new Cat("Zuz", 1, "Парская")
            , new Cat("Dak", 3, "flld")));

    @Test
    @DisplayName("Добавление cat")
    void createCat() throws Exception {

        when(service.addCat(catTest)).thenReturn(catTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/cats")
                        .content(mapper.writeValueAsBytes(catTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Luiza"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("Еркширская"))
                .andExpect(status().isOk());
        verify(service, only()).addCat(catTest);
    }

    @Test
    @DisplayName("All cats")
    void getAllCats() throws Exception {
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/cats")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).getAll();
    }

    @Test
    @DisplayName("Поиск cat по id")
    void findById() throws Exception {
        when(service.findCat(anyLong())).thenReturn(catTest);

        mockMvc.perform(MockMvcRequestBuilders.get("/cats/{id}", catTest.getId()))
                .andExpect(status().isOk());

        verify(service, only()).findCat(catTest.getId());
    }

    @Test
    @DisplayName("Изменение cat")
    void editCat() throws Exception {
        when(service.editCat(catTest)).thenReturn(catTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/cats")
                        .content(mapper.writeValueAsBytes(catTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Luiza"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("Еркширская"))
                .andExpect(status().isOk());

        verify(service, only()).editCat(catTest);
    }

    @Test
    @DisplayName("Удаление Cat")
    void deleteCat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/cats/0")
                .content(mapper.writeValueAsBytes(catTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
