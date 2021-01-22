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
    private String id;
    private String description;
    private String genre;
    private String title;
    private GeoPoint location;
    private String location_string;

    //private ArrayList<Seat> lSeat;
    private ArrayList<PriceCategory> lPriceCategories;

    private int total_seats = 0;
    private int available_seats;
    private Calendar cal;

    private int year;
    private int month;
    private int day;
    private int hourofDay;
    private int minute;

    private String photoStoragePath;

    public Event() {

        this.cal = new GregorianCalendar();
        this.cal.setTimeZone(TimeZone.getTimeZone("Romania"));
        String[] weekdays = new DateFormatSymbols().getWeekdays(); // Get day names
        this.day_name = weekdays[cal.get(DAY_OF_WEEK)];
    }

    public Event(String title, String genre, String description, String location_string, int year, int month, int day,
                 int hourOfDay, int minute, double latitude, double longitude, ArrayList<PriceCategory> priceCategories) {

        this.title = title;
        this.description = description;
        this.location_string = location_string;
        this.genre = genre;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hourofDay = hourOfDay;
        this.minute = minute;
        this.lPriceCategories = priceCategories;


        this.cal = new GregorianCalendar(year, month, day, hourOfDay, minute);
        this.cal.setTimeZone(TimeZone.getTimeZone("Romania"));


        String[] weekdays = new DateFormatSymbols().getWeekdays(); // Get day names
        this.day_name = weekdays[cal.get(DAY_OF_WEEK)];


        this.location = new GeoPoint(latitude, longitude);

        for (PriceCategory pcat : this.lPriceCategories) {
            this.total_seats += pcat.getSeats_in_category();

        }
    }


    public void addPriceCategory(PriceCategory pc1) {
        lPriceCategories.add(pc1);

    }

    public ArrayList<PriceCategory> getlPriceCategories() {
        return lPriceCategories;
    }

    public void setlPriceCategories(ArrayList<PriceCategory> lPriceCategories) {
        this.lPriceCategories = lPriceCategories;
    }

    public int getTotalSeats() {
        return total_seats;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation_string() {
        return location_string;
    }

    public void setLocation_string(String location_string) {
        this.location_string = location_string;
    }

    public int getHourofDay() {
        return hourofDay;
    }

    public void setHourofDay(int hourofDay) {
        this.hourofDay = hourofDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoStoragePath() {
        return photoStoragePath;
    }

    public void setPhotoStoragePath(String photoStoragePath) {
        this.photoStoragePath = photoStoragePath;
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

    public String getData_string() {

        String ziua_str = "eroare", luna_str = "eroare";

        switch (day_name) {
            case "Monday":
                ziua_str = "luni";
                break;
            case "Tuesday":
                ziua_str = "marți";
                break;
            case "Wednesday":
                ziua_str = "miercuri";
                break;
            case "Thursday":
                ziua_str = "joi";
                break;
            case "Friday":
                ziua_str = "vineri";
                break;
            case "Saturday":
                ziua_str = "sâmbătă";
                break;
            case "Sunday":
                ziua_str = "duminică";
                break;

        }

        switch (month) {
            case 1:
                luna_str = "ianuarie";
                break;
            case 2:
                luna_str = "februarie";
                break;
            case 3:
                luna_str = "martie";
                break;
            case 4:
                luna_str = "aprilie";
                break;
            case 5:
                luna_str = "mai";
                break;
            case 6:
                luna_str = "iunie";
                break;
            case 7:
                luna_str = "iulie";
                break;
            case 8:
                luna_str = "august";
                break;
            case 9:
                luna_str = "septembrie";
                break;
            case 10:
                luna_str = "octombrie";
                break;
            case 11:
                luna_str = "noiembrie";
                break;
            case 12:
                luna_str = "decembrie";
                break;
        }


        return ziua_str + " " + day + " " + luna_str;
    }

    public String getTime_string() {
        if (minute == 0) {
            return hourofDay + ":" + minute + "0";

        }
        return hourofDay + ":" + minute;
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
