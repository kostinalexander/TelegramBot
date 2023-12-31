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

    private Boolean reportChecked;

    @OneToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    public ReportDog(byte[] photo, String text, LocalDate localDate, Boolean reportChecked) {
        super(photo, text, localDate);
        this.reportChecked = null;
    }

    public ReportDog() {
    }

    public void setReportChecked(Boolean reportChecked) {
        this.reportChecked = reportChecked;
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
