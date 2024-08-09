package dev.brianmiller.restclient.setlistfm.data;

import java.util.ArrayList;

public class UserAttendedResponse {

    public int code;
    public String status;
    public String message;
    public String type;
    public int itemsPerPage;
    public int page;
    public int total;
    public ArrayList<Setlist> setlist;
}
