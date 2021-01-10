package com.example.eventbookingsystem_fseproject;

import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_WEEK;

public class Event {
//TODO: Adauga variablia pt imaginea evenimentului conform imaginilor stocate in DB.

    private final String day_name;
    private String name;

    private String description;
    private String genre;
    private GeoPoint location;
    private int price_categories;
    // lSeat = list of seats
    private ArrayList<Seat> lSeat;
    private int total_seats;
    private int available_seats;
    private Calendar cal;
    private int year;
    private int month;
    private int day;


    public Event(String name, String genre, String description, int year, int month, int day,
                 int hourOfDay, int minute, double latitude, double longitude, int nr_price_categories) {

        this.name = name;
        this.description = description;
        this.genre = genre;
        this.year = year;
        this.month = month;
        this.day = day;

        this.cal = new GregorianCalendar(year, month, day, hourOfDay, minute);
        this.cal.setTimeZone(TimeZone.getTimeZone("Romania"));


        String[] weekdays = new DateFormatSymbols().getWeekdays(); // Get day names
        this.day_name = weekdays[cal.get(DAY_OF_WEEK)];


        this.location = new GeoPoint(latitude, longitude);

        this.price_categories = nr_price_categories;
        lSeat = new ArrayList<Seat>();
    }


    public void setPriceCategory(int category_nr, String category_name, double seat_price, int nr_seats) throws IndexOutOfBoundsException {

        int count = 1;

        if (category_nr > price_categories) {
            throw new IndexOutOfBoundsException("Categoria aleasa nu exista pt acest Event!");
        }

        while (count <= nr_seats) {

            Seat s1 = new Seat(seat_price);
            s1.setName(category_name + "_" + count);

            lSeat.add(s1);
            this.total_seats++;

            count++;
        }

    }

    public int getTotalSeats() {
        return total_seats;
    }

    public void printAllSeats() {
        String status;

        for (Seat s : lSeat) {
            if (s.isAvailable()) {
                status = "available";
            } else {
                status = "taken";
            }

            System.out.println(s.getName() + " " + s.getPrice() + " " + status);
        }


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }

    public String getDay_name() {
        return day_name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void printDate() {

        String month_name = "error";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (cal.get(Calendar.MONTH) >= 0 && cal.get(Calendar.MONTH) <= 11) {
            // bag -1 pt ca in cod lunile incep de la 0 dar in constructor incepem de la 1, omeneste
            month_name = months[cal.get(Calendar.MONTH) - 1];
        }


        System.out.println(day_name
                + " , " + cal.get(Calendar.DAY_OF_MONTH)
                //+" , "+ cal.get(Calendar.MONTH)
                + " , " + month_name
                + " , " + cal.get(Calendar.YEAR)
                + " , " + cal.get(Calendar.HOUR_OF_DAY)
                + ":" + cal.get(Calendar.MINUTE));


    }


    public void autoSeatSelect(String selected_category_name, int nr_seats) {


        //TODO: Completeaza autoSeatSelect, care returneaza Seat-urile consecutive libere in
        // functie de nr_seats ales.


    }

    public void setCal(GregorianCalendar cal) {
        this.cal = cal;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
