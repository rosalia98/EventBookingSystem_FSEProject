package com.example.eventbookingsystem_fseproject;

public class Seat {

    // Owner_id se va modifica atunci cand userul finalizeaza plata pt un seat...
    // .. seat-ul va deveni is_available=false si owner_id va lua id user din firebase.
    private String owner_id;
    private String name;
    private double price;
    private boolean is_available;


    public Seat(double price) {

        this.price = price;
        this.is_available = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setAvailability(boolean isAvailable) {
        this.is_available = isAvailable;
    }

    public boolean isAvailable() {
        return is_available;
    }
}
