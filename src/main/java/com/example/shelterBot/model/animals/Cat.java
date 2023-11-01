package com.example.shelterBot.model.animals;

import com.example.shelterBot.model.people.Users;
import com.example.shelterBot.model.shelter.ShelterForCats;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "cats")
public class Cat extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
  //  @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shelter_cats_id")
    private ShelterForCats shelterCats;
  //  @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner",nullable = true)
    private Users users;

    public Cat(String name, int age, String breed) {
        super(name, age, breed);
    }

    public Cat() {
    }

    public void setShelterCats(ShelterForCats shelterCats) {
        this.shelterCats = shelterCats;
    }

    public void setUsers(Users users) {
        this.users = users;
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
