package com.example.shelterBot.model.shelter;

import com.example.shelterBot.model.animals.Cat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "shelter_for_cats")
public class ShelterForCats extends Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @OneToMany(mappedBy = "shelterCats")
    private List<Cat> cats;

    public ShelterForCats(String nameShelter, String address, String workingHours) {
        super(nameShelter, address, workingHours);
    }

    public ShelterForCats() {

    }

    public long getId() {
        return id;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShelterForCats that = (ShelterForCats) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShelterForCats{" +
                "id=" + id +
                super.toString() +
                ", cats=" + cats +
                '}';
    }
}