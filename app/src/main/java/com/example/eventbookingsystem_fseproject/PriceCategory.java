package com.example.eventbookingsystem_fseproject;

import java.util.ArrayList;

public class PriceCategory {

    private String cat_name;
    private int rows;
    private int seats_per_row;
    private double price_per_seat;
    private ArrayList<Seat> lSeats_in_category;

    private int seats_in_category;


    public PriceCategory() {
    }

    public PriceCategory(String cat_name, int rows, int seats_per_row, double price_per_seat) {
        this.cat_name = cat_name;
        this.rows = rows;
        this.seats_per_row = seats_per_row;
        this.price_per_seat = price_per_seat;
        //delete

        lSeats_in_category = new ArrayList<Seat>();

        int row_count = 1;
        int seat_count = 1;

        for (row_count = 1; row_count <= this.rows; row_count++) {
            for (seat_count = 1; seat_count <= this.seats_per_row; seat_count++) {

                String seat_id = cat_name + "_row" + row_count + "_seat" + seat_count;
                Seat s = new Seat(seat_id, cat_name, row_count, seat_count, price_per_seat, true);

                lSeats_in_category.add(s);

            }
        }

        seats_in_category = lSeats_in_category.size();


    }

    public ArrayList<Seat> getlSeats_in_category() {
        return lSeats_in_category;
    }

    public void setlSeats_in_category(ArrayList<Seat> lSeats_in_category) {
        this.lSeats_in_category = lSeats_in_category;
    }

    public int getSeats_in_category() {
        return seats_in_category;
    }

    public void setSeats_in_category(int seats_in_category) {
        this.seats_in_category = seats_in_category;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeats_per_row() {
        return seats_per_row;
    }

    public void setSeats_per_row(int seats_per_row) {
        this.seats_per_row = seats_per_row;
    }

    public double getPrice_per_seat() {
        return price_per_seat;
    }

    public void setPrice_per_seat(double price_per_seat) {
        this.price_per_seat = price_per_seat;
    }
}
