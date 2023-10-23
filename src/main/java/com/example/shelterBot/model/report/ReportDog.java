package com.example.shelterBot.model.report;

import com.example.shelterBot.model.animals.Dog;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
public class ReportDog extends Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    public ReportDog(String photo, String text, LocalDate localDate, Boolean reportChecked) {
        super(photo, text, localDate, reportChecked);
    }

    public ReportDog() {
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDog reportDog = (ReportDog) o;
        return Objects.equals(id, reportDog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportDog{" +
                "id=" + id +
                super.toString() +
                ", dog=" + dog +
                '}';
    }
}
