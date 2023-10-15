package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.repository.CatRepository;
import com.example.shelterBot.service.CatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatServiceTests {


    @Mock
    private CatRepository repository;

    @InjectMocks
    private CatService service;

    Cat cat = new Cat("Luis", 1, "Пернский");

    public static final List<Cat> list = List.of(
            new Cat("Luka", 2, "Польский"),
            new Cat("Ryk", 1, "Портский")
    );

    @Test
    @DisplayName("Получить всех cat")
    void allCatsTest(){
          when(repository.findAll())
                  .thenReturn(list);

          assertIterableEquals(list,service.getAll());
    }

    @Test
    @DisplayName("Создание cat")
    void addCatTest(){

        when(service.addCat(cat)).thenReturn(cat);

        assertEquals(cat,service.addCat(cat));
    }

    @Test
    @DisplayName("Edit cat")
    void editCatTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(cat));
        Cat cat1 = service.editCat(cat);
        Assertions.assertThat(cat1).isEqualTo(cat);
    }

}
