package dev.brianmiller.restclient.setlistfm.data;

public record Setlist(String id, String versionId, String eventDate,
                      String lastUpdated, Artist artist, Venue venue,
                      Tour tour, String url) {

}
