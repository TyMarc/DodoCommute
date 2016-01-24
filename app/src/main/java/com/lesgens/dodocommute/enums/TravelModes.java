package com.lesgens.dodocommute.enums;


public enum TravelModes {
    DRIVING("driving"),
    WALKING("walking"),
    BICYCLING("bicycling"),
    TRANSIT("transit");

    private final String stringValue;

    TravelModes(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static TravelModes fromString(String role) {
        if (role == null) return null;
        else if (role.equals("driving")) return DRIVING;
        else if (role.equals("walking")) return WALKING;
        else if (role.equals("bicycling")) return BICYCLING;
        else if (role.equals("transit")) return TRANSIT;
        return null;
    }
}
