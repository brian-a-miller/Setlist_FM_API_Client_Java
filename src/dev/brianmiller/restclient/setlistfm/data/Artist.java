package dev.brianmiller.restclient.setlistfm.data;

public record Artist(String mbid, String name, String sortName,
                     String disambiguation, String url) {
}
