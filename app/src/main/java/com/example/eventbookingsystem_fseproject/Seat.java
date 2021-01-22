package com.example.eventbookingsystem_fseproject;

public class Seat {

    // Owner_id se va modifica atunci cand userul finalizeaza plata pt un seat...
    // .. seat-ul va deveni is_available=false si owner_id va lua id user din firebase.

    private String owner_id;
    private String seat_id;
    private String category_name;
    private int row_number;
    private int seat_number;
    private double price;
    private boolean is_available;

    public Seat() {
    }

    public Seat(String seat_id, String category_name, int row_number, int seat_number, double price, boolean is_available) {
        this.seat_id = seat_id;
        this.category_name = category_name;
        this.row_number = row_number;
        this.seat_number = seat_number;
        this.price = price;
        this.is_available = is_available;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public int getRow_number() {
        return row_number;
    }

    public void setRow_number(int row_number) {
        this.row_number = row_number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }
}
