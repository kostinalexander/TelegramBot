package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.VolunteerController;
import com.example.shelterBot.model.Users;
import com.example.shelterBot.model.Volunteer;
import com.example.shelterBot.service.UsersService;
import com.example.shelterBot.service.VolunteerService;
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

@WebMvcTest(VolunteerController.class)
public class ControllerVolunteerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService service;

    @Autowired
    private ObjectMapper mapper;

    static Volunteer test = new Volunteer("Tolya", "Harin", "bb@mail.ru" );

    List<Volunteer> list = new ArrayList<>(List.of(new Volunteer("Senya", "Sgrrin", "zz@mail.ru" )
            , new Volunteer("Fedya", "Lalan", "kb@mail.ru" )
            , new Volunteer("Denya", "Harinaas", "gh@mail.ru" )));

    @Test
    @DisplayName("Добавление volunteer")
    void createVol() throws Exception {

        when(service.add(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tolya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Harin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bb@mail.ru"))
                .andExpect(status().isOk());
        verify(service, only()).add(test);
    }

    @Test
    @DisplayName("All vol")
    void getAllVol() throws Exception {
        when(service.allVolunteer()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/volunteer")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).allVolunteer();
    }



    @Test
    @DisplayName("Изменение vol")
    void editVol() throws Exception {
        when(service.edit(test)).thenReturn(test);

        mockMvc.perform(MockMvcRequestBuilders.put("/volunteer")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tolya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Harin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bb@mail.ru"))
                .andExpect(status().isOk());

        verify(service, only()).edit(test);
    }

    @Test
    @DisplayName("Удаление vol")
    void deleteVol() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/0")
                        .content(mapper.writeValueAsBytes(test))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
