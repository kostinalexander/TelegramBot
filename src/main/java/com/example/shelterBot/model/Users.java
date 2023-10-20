package com.example.shelterBot.model;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.model.animals.Dog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private int age;
    private int numberPhone;
    private String email;
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Cat> cats;
    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Dog> dogs;

    public Users() {
    }

    public Users(String firstName, String lastName, int age, int numberPhone, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.numberPhone = numberPhone;
        this.email = email;
        this.address = address;
    }


    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public void setFirstLoginDate(LocalDateTime firstLoginDate) {
        this.firstLoginDate = firstLoginDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNumberPhone(int numberPhone) {
        this.numberPhone = numberPhone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", telegramUserId=" + telegramUserId +
                ", firstLoginDate=" + firstLoginDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", numberPhone=" + numberPhone +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", cats=" + cats +
                ", dogs=" + dogs +
                '}';
    }
}