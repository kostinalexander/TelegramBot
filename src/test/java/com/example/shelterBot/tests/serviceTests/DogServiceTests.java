package com.example.shelterBot.tests.serviceTests;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.repository.CatRepository;
import com.example.shelterBot.repository.DogRepository;
import com.example.shelterBot.service.CatService;
import com.example.shelterBot.service.DogService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class DogServiceTests {

    @Mock
    private DogRepository repository;

    @InjectMocks
    private DogService service;

    Dog dog = new Dog("Luis", 1, "Пернский");

    public static final List<Dog> list = List.of(
            new Dog("Luka", 2, "Польский"),
            new Dog("Ryk", 1, "Портский")
    );

    @Test
    @DisplayName("Получить всех dog")
    void allCatsTest(){
        when(repository.findAll())
                .thenReturn(list);

        assertIterableEquals(list,service.getAll());
    }

    @Test
    @DisplayName("Создание dog")
    void addCatTest(){

        when(service.addDog(dog)).thenReturn(dog);

        assertEquals(dog,service.addDog(dog));
    }

    @Test
    @DisplayName("Edit dog")
    void editCatTest(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(dog));
        Dog dog1 = service.editDog(dog);
        Assertions.assertThat(dog1).isEqualTo(dog);
    }

}
