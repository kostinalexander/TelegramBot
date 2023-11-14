package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.CatShController;
import com.example.shelterBot.model.shelter.ShelterForCats;
import com.example.shelterBot.service.CatShService;
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

@WebMvcTest(CatShController.class)
public class ControllerCatsShTests  {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatShService service;

    @Autowired
    private ObjectMapper mapper;

    static ShelterForCats test = new ShelterForCats("nameSh","zar", LocalTime.NOON);

    List<ShelterForCats> list = new ArrayList<>(List.of(new ShelterForCats("nameSh1", "zag", LocalTime.NOON)
            , new ShelterForCats("nameSh2", "ger", LocalTime.NOON)
            , new ShelterForCats("nameSh3", "rar", LocalTime.NOON)));

    @Test
    @DisplayName("Добавление catSh")
    void createCatSh() throws Exception {

        when(service.addCatSh(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.post("/cats_shelter")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameShelter").value("nameSh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("zar"))
                .andExpect(status().isOk());
        verify(service, only()).addCatSh(test);
    }

    @Test
    @DisplayName("All catSh")
    void getAllCatsSh() throws Exception {
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/cats_shelter")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).getAll();
    }

    @Test
    @DisplayName("Поиск catSh по id")
    void findById() throws Exception {
        when(service.findShelterForCats(anyLong())).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.get("/cats_shelter/{id}", test.getId()))
                .andExpect(status().isOk());

        verify(service, only()).findShelterForCats(test.getId());
    }

    @Test
    @DisplayName("Изменение catSh")
    void editCatSh() throws Exception {
        when(service.editCatShelter(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.put("/cats_shelter")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameShelter").value("nameSh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("zar"))
                .andExpect(status().isOk());

        verify(service, only()).editCatShelter(test);
    }

    @Test
    @DisplayName("Удаление CatShelter")
    void deleteCatSh() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cats_shelter/0")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
