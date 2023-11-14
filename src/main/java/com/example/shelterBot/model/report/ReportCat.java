package com.example.shelterBot.model.report;

import com.example.shelterBot.model.animals.Cat;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@Table(name = "report_cat")
public class ReportCat extends Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean reportChecked;

    @OneToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;




    public ReportCat(byte[] photo, String text, LocalDate localDate,Boolean reportChecked) {
        super(photo, text, localDate);
        this.reportChecked=false;
    }

    public ReportCat() {
    }

    public void setReportChecked(Boolean reportChecked) {
        this.reportChecked = reportChecked;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportCat report = (ReportCat) o;
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
                super.toString() +
                ", cat=" + cat +
                '}';
    }
}
