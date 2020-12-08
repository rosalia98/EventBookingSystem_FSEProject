package com.example.eventbookingsystem_fseproject;
import com.google.firebase.firestore.GeoPoint;

public class User {
    private String prenume, nume, email, telefon, adresa_string;
    private GeoPoint adresa_geopoint;


    public User(){

    }

    public User(String prenume, String nume, String email, String telefon, String adresa_string) {

        this.email=email;
        this.prenume = prenume;
        this.nume = nume;
        this.telefon = telefon;
        this.adresa_string = adresa_string;
        this.adresa_geopoint = new GeoPoint(0,0);

        //this.adresa_map.setLongitude(0);
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa_string() {
        return adresa_string;
    }

    public void setAdresa_string(String adresa_string) {
        this.adresa_string = adresa_string;
    }

    public GeoPoint getAdresa_geopoint() {
        return adresa_geopoint;
    }

    public void setAdresa_geopoint(double lat, double lng) {
        this.adresa_geopoint = new GeoPoint(lat,lng);
    }
}
