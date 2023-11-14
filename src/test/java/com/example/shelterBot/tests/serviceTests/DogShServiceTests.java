package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.model.shelter.DogShelter;
import com.example.shelterBot.repository.DogRepository;
import com.example.shelterBot.repository.DogShRepository;
import com.example.shelterBot.service.DogService;
import com.example.shelterBot.service.DogShService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class DogShServiceTests {

    @Mock
    private DogShRepository repository;

    @InjectMocks
    private DogShService service;

    DogShelter shelter = new DogShelter("Luis", "kdd", LocalTime.NOON);

    public static final List<DogShelter> list = List.of(
            new DogShelter("Luka", "dkk", LocalTime.NOON),
            new DogShelter("Ryk", "dds", LocalTime.NOON)
    );

    @Test
    @DisplayName("Получить всех dogSHelters")
    void allDogShTest(){
        when(repository.findAll())
                .thenReturn(list);

        assertIterableEquals(list,service.getAll());
    }

    @Test
    @DisplayName("Создание dogSh")
    void addDogShTest(){

        when(service.addDogSh(shelter)).thenReturn(shelter);

        assertEquals(shelter,service.addDogSh(shelter));
    }

    @Test
    @DisplayName("Edit dogSh")
    void editCatTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(shelter));
        DogShelter shelter1 = service.editDogShelter(shelter);
        Assertions.assertThat(shelter1).isEqualTo(shelter1);
    }
}
