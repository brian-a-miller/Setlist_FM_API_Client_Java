package dev.brianmiller.restclient.setlistfm.data;

public record Venue(String id, String name, City city, String url) {

    public String getCityName() {
        if (city == null) {
            return "Unknown";
        }
        return city.name();
    }

    public String getStateName() {
        if (city == null) {
            return "Unknown";
        }
        return city.state();
    }

    public String getStateCode() {
        if (city == null) {
            return "Unknown";
        }
        return city.stateCode();
    }

    public String getCountryName() {
        if (city == null || city.country() == null) {
            return "Unknown";
        }
        return city.country().name();
    }

    public String getCountryCode() {
        if (city == null || city.country() == null) {
            return "Unknown";
        }
        return city.country().code();
    }
}
