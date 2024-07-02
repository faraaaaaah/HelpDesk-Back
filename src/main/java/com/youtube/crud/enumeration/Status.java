package com.youtube.crud.enumeration;

public enum Status {
    DRAFT("DRAFT"),
    IN_PROGRESS("IN_PROGRESS"),
    TREATED("TREATED");

    private final String status;

    // Constructor
    Status(String status) {
        this.status = status;
    }

    // Getter method
    public String getStatus() {
        return this.status;
    }
}
