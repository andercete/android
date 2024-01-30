package com.example.comercial;

// Evento.java
public class Evento {
    private int id; // Added id property
    private String title;
    private String date;
    private String time;
    private String description;

    // Default constructor
    public Evento() {
    }

    // Constructor with parameters
    public Evento(int id, String title, String date, String time, String description) {
        this.id = id;
        this.title = title;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    // Setter methods (if needed)

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
