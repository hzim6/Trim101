package com.example.trim101;

public class Barber {
    private String barberId;
    private String username;


    public Barber(String barberId, String username) {
        this.barberId = barberId;
        this.username = username;
    }
    public String getId() {
        return barberId;
    }
    public String getUsername() {
        return username;
    }

}

