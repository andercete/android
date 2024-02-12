package com.example.comercial.BBDD;



// Evento.java
public class Evento {
    private int id; // Added id property
    private String title;
    private String location; // Added location property
    private String date;
    private String time;
    private String description;

    // Default constructor
    public Evento() {
    }

    // Constructor with parameters
    public Evento(int id, String title, String location, String date, String time, String description) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

}
