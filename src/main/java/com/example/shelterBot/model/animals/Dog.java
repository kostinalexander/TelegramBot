package com.example.shelterBot.model.animals;

import com.example.shelterBot.model.Users;
import com.example.shelterBot.model.shelter.DogShelter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "dogs")
public class Dog extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shelter_dog_id")
    private DogShelter dogShelter;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private Users users;

    public Dog(String name, int age, String breed) {
        super(name, age, breed);
    }

    public Dog() {

    }

    public long getId() {
        return id;
    }

    public DogShelter getDogShelter() {
        return dogShelter;
    }

    public void setDogShelter(DogShelter dogShelter) {
        this.dogShelter = dogShelter;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return id == dog.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                super.toString() +
                ", dogShelter=" + dogShelter +
                ", owner= " + users +
                '}';
    }
}