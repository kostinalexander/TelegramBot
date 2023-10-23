package com.example.shelterBot.model.animals;

import com.example.shelterBot.model.people.Users;
import com.example.shelterBot.model.shelter.ShelterForCats;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cats")
public class Cat extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shelter_cats_id")
    private ShelterForCats shelterCats;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private Users users;

    public Cat(String name, int age, String breed) {
        super(name, age, breed);
    }

    public Cat() {
    }

    public long getId() {
        return id;
    }

    public ShelterForCats getShelterCats() {
        return shelterCats;
    }

    public void setShelterCats(ShelterForCats shelterCats) {
        this.shelterCats = shelterCats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return id == cat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                super.toString() +
                ", shelterCats=" + shelterCats +
                ", owner=" + users +
                '}';
    }
}
