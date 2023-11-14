package com.example.shelterBot.model.shelter;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalTime;

@MappedSuperclass
public abstract class Shelter {
    private String nameShelter;
    private String address;
    private String workingHours;

    public Shelter(String nameShelter, String address, String workingHours) {
        this.nameShelter = nameShelter;
        this.address = address;
        this.workingHours = workingHours;
    }

    public Shelter() {

    }

    public String getNameShelter() {
        return nameShelter;
    }

    public void setNameShelter(String nameShelter) {
        this.nameShelter = nameShelter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String toString() {
        return "nameShelter='" + nameShelter + '\'' +
                ", address='" + address + '\'' +
                ", workingHours=" + workingHours +
                '}';
    }
}

