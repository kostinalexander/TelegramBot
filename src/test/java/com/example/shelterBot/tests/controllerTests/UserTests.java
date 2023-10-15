package com.example.shelterBot.tests.controllerTests;

import com.example.shelterBot.controller.UsersController;
import com.example.shelterBot.model.Users;
import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.service.CatService;
import com.example.shelterBot.service.UsersService;
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

@WebMvcTest(UsersController.class)
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService service;

    @Autowired
    private ObjectMapper mapper;

    static Users userTest = new Users("Vasya", "Petin", 13,892045292,"bok@mail.ru", "vr" );

    List<Users> list = new ArrayList<>(List.of(new Users("Luz","Tem", 21,932923, "fkel@mail.ru", "vk")
            , new Users("Igor", "Sidorov", 13,892335292,"kok@mail.ru", "vr")
            , new Users("Yana", "Petrova", 13,89425292,"sok@mail.ru", "vr")));

    @Test
    @DisplayName("Добавление user")
    void createUser() throws Exception {

        when(service.addUser(userTest)).thenReturn(userTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(mapper.writeValueAsBytes(userTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Vasya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Petin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberPhone").value(892045292))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bok@mail.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("vr"))
                .andExpect(status().isOk());
        verify(service, only()).addUser(userTest);
    }

    @Test
    @DisplayName("All users")
    void getAllCats() throws Exception {
        when(service.allUsers()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(status().isOk());
        verify(service, only()).allUsers();
    }



    @Test
    @DisplayName("Изменение user")
    void editUser() throws Exception {
        when(service.editUser(userTest)).thenReturn(userTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .content(mapper.writeValueAsBytes(userTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Vasya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Petin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberPhone").value(892045292))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bok@mail.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("vr"))
                .andExpect(status().isOk());

        verify(service, only()).editUser(userTest);
    }

    @Test
    @DisplayName("Удаление User")
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/0")
                        .content(mapper.writeValueAsBytes(userTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
