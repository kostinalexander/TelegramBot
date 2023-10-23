package com.example.shelterBot.model.report;

import java.time.LocalDate;

public class Report {
    private String photo;
    private String text;
    private LocalDate localDate;
    private Boolean reportChecked;

    public Report(String photo, String text, LocalDate localDate, Boolean reportChecked) {
        this.photo = photo;
        this.text = text;
        this.localDate = localDate;
        this.reportChecked = reportChecked;
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
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getLocalDate() {
        return localDate;
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

    @Override
    public String toString() {
        return "Report{" +
                "photo='" + photo + '\'' +
                ", text='" + text + '\'' +
                ", localDate=" + localDate +
                ", reportChecked=" + reportChecked +
                '}';
    }
}
