package com.example.shelterBot.model;

import com.example.shelterBot.model.animals.Dog;
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
    private Boolean reportChecked;

    @OneToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;


    public Report(String photo, String text, LocalDate localDate, Dog dog,Boolean reportChecked) {
        this.photo = photo;
        this.text = text;
        this.localDate = localDate;
        this.dog = dog;
        this.reportChecked= null;
    }

    public Report() {

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

    public Boolean getReportChecked() {
        return reportChecked;
    }

    public void setReportChecked(Boolean reportChecked) {
        this.reportChecked = reportChecked;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Long getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getText() {
        return text;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public Dog getDog() {
        return dog;
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
                ", dog=" + dog +
                '}';
    }
}
