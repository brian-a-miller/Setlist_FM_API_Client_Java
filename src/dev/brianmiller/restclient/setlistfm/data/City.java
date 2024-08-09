package dev.brianmiller.restclient.setlistfm.data;

public record City(String id, String name, String state,
                   String stateCode, Coords coords, Country country) {
}
