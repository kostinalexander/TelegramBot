package com.example.shelterBot.model.report;

import java.time.LocalDate;

public class Report {
    private String photo;
    private String reportText;
    private LocalDate localDate;

    public Report(String photo, String reportText, LocalDate localDate ) {
        this.photo = photo;
        this.reportText = reportText;
        this.localDate = localDate;
    }

    public Report() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
