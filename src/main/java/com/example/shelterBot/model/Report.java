package com.example.shelterBot.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String text;
    private LocalDate localDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;


    public Report(String photo, String text, LocalDate localDate, Users users) {
        this.photo = photo;
        this.text = text;
        this.localDate = localDate;
        this.users = users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setUsers(Users users) {
        this.users = users;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", text='" + text + '\'' +
                ", localDate=" + localDate +
                ", users=" + users +
                '}';
    }
}
