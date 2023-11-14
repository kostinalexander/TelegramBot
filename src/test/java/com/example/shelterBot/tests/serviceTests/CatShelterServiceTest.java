package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.model.shelter.ShelterForCats;
import com.example.shelterBot.repository.CatRepository;
import com.example.shelterBot.repository.CatShRepository;
import com.example.shelterBot.service.CatService;
import com.example.shelterBot.service.CatShService;
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
public class CatShelterServiceTest {
    @Mock
    private CatShRepository repository;

    @InjectMocks
    private CatShService service;

    ShelterForCats shelter = new ShelterForCats("Luis", "kkd", LocalTime.NOON);

    public static final List<ShelterForCats> list = List.of(
            new ShelterForCats("Luka", "ldd", LocalTime.NOON),
            new ShelterForCats("Ryk", "dd", LocalTime.NOON)
    );

    @Test
    @DisplayName("Получить всех catSh")
    void allCatsShTest(){
        when(repository.findAll())
                .thenReturn(list);

        assertIterableEquals(list,service.getAll());
    }

    @Test
    @DisplayName("Создание catSH")
    void addCatSHTest(){

        when(service.addCatSh(shelter)).thenReturn(shelter);

        assertEquals(shelter,service.addCatSh(shelter));
    }

    @Test
    @DisplayName("Edit catSH")
    void editCatShTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(shelter));
        ShelterForCats shelter1 = service.editCatShelter(shelter);
        Assertions.assertThat(shelter1).isEqualTo(shelter);
    }
}
