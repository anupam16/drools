package com.droolsproject.droolspro.model;

public class AHUEvent {
    private String type; // Type of event, e.g., "CoolingFailure"

    public AHUEvent(String type) {
        this.type = type;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
