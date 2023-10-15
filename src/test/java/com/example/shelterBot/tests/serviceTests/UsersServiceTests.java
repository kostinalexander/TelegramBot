package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.Users;
import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.repository.CatRepository;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.service.CatService;
import com.example.shelterBot.service.UsersService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {
    @Mock
    private UsersRepository repository;

    @InjectMocks
    private UsersService service;

    Users user  = new Users("Vasya", "Petin", 13,892045292,"bok@mail.ru", "vr");

    List<Users> list = new ArrayList<>(List.of(new Users("Luz","Tem", 21,932923, "fkel@mail.ru", "vk")
            , new Users("Igor", "Sidorov", 13,892335292,"kok@mail.ru", "vr")
            , new Users("Yana", "Petrova", 13,89425292,"sok@mail.ru", "vr")));

    @Test
    @DisplayName("Получить всех users")
    void allUsersTest(){
        when(repository.findAll())
                .thenReturn(list);

        assertIterableEquals(list,service.allUsers());
    }

    @Test
    @DisplayName("Создание user")
    void addCatTest(){

        when(service.addUser(user)).thenReturn(user);

        assertEquals(user,service.addUser(user));
    }

    @Test
    @DisplayName("Edit cat")
    void editCatTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        Users user1 = service.editUser(user);
        Assertions.assertThat(user1).isEqualTo(user);
    }
}
