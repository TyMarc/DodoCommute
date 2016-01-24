package com.lesgens.dodocommute.enums;


public enum TransitMode {
    BUS("bus"),
    SUBWAY("subway"),
    TRAIN("train"),
    TRAM("tram"),
    RAIL("rail");

    private final String stringValue;

    TransitMode(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static TransitMode fromString(String role) {
        if (role == null) return null;
        else if (role.equals("bus")) return BUS;
        else if (role.equals("subway")) return SUBWAY;
        else if (role.equals("train")) return TRAIN;
        else if (role.equals("tram")) return TRAM;
        else if (role.equals("rail")) return RAIL;
        return null;
    }
}
