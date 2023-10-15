package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.Users;
import com.example.shelterBot.model.Volunteer;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.repository.VolunteerRepository;
import com.example.shelterBot.service.UsersService;
import com.example.shelterBot.service.VolunteerService;
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
public class VolunteerServiceTests {

    @Mock
    private VolunteerRepository repository;

    @InjectMocks
    private VolunteerService service;

    Volunteer volunteer  = new Volunteer("Tolya", "Harin", "bb@mail.ru");

    List<Volunteer> list = new ArrayList<>(List.of(new Volunteer("Senya", "Sgrrin", "zz@mail.ru" )
            , new Volunteer("Fedya", "Lalan", "kb@mail.ru" )
            , new Volunteer("Denya", "Harinaas", "gh@mail.ru" )));

    @Test
    @DisplayName("Получить всех vol")
    void allVolTest(){
        when(repository.findAll())
                .thenReturn(list);

        assertIterableEquals(list,service.allVolunteer());
    }

    @Test
    @DisplayName("Создание vol")
    void addVolTest(){

        when(service.add(volunteer)).thenReturn(volunteer);

        assertEquals(volunteer,service.add(volunteer));
    }

    @Test
    @DisplayName("Edit vol")
    void editVolTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(volunteer));
        Volunteer volunteer1 = service.edit(volunteer);
        Assertions.assertThat(volunteer1).isEqualTo(volunteer);
    }
}
