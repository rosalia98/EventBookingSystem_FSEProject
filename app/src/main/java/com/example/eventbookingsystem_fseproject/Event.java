package com.example.eventbookingsystem_fseproject;

import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_WEEK;

public class Event {
//TODO: Adauga variablia pt imaginea evenimentului conform imaginilor stocate in DB.

    Calendar cal;
    private String name;
    private String description;
    private String day_name;
    private GeoPoint location;

//TODO: Rezolva Seat.java si adauga aici sub forma de HashMap probabil
    //private ArrayList<Seat>;

    public Event(String name, String description, int year, int month, int day, int hourOfDay, int minute, double latitude, double longitude) {

        this.name = name;
        this.description = description;

        this.cal = new GregorianCalendar(year, month, day, hourOfDay, minute);
        this.cal.setTimeZone(TimeZone.getTimeZone("Romania"));

        String[] weekdays = new DateFormatSymbols().getWeekdays(); // Get day names
        this.day_name = weekdays[cal.get(DAY_OF_WEEK)];

        this.location = new GeoPoint(latitude, longitude);
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


    public void printDate() {

        String month_name = "wrong";
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
