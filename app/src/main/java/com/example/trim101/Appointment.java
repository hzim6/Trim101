package com.example.trim101;

public class Appointment {
    private String customerName;
    private String dateRequested;
    private String timeRequested;

    public Appointment() {
        // Empty constructor needed for Firestore
    }

    public Appointment(String customerName, String dateRequested, String timeRequested) {
        this.customerName = customerName;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public String getTimeRequested() {
        return timeRequested;
    }
}

