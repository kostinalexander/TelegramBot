package com.example.shelterBot.model.report;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;
@MappedSuperclass
public class Report {
    private byte[] photo;
    private String reportText;
    private LocalDate localDate;

    public Report(byte[] photo, String reportText, LocalDate localDate ) {
        this.photo = photo;
        this.reportText = reportText;
        this.localDate = localDate;
    }

    public Report() {
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public String getText() {
        return reportText;
    }

    public void setText(String text) {
        this.reportText = text;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }



    @Override
    public String toString() {
        return "Report{" +
                "photo='" + photo + '\'' +
                ", text='" + reportText + '\'' +
                ", localDate=" + localDate +
                '}';
    }
}
