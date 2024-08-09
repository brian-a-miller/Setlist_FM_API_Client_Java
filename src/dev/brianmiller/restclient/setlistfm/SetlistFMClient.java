package dev.brianmiller.restclient.setlistfm;

import com.google.gson.Gson;

import com.google.gson.JsonSyntaxException;
import dev.brianmiller.properties.Properties;
import dev.brianmiller.restclient.RestClient;
import dev.brianmiller.restclient.RestResponse;
import dev.brianmiller.restclient.setlistfm.data.UserAttendedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class SetlistFMClient {

    private final static String HEADER_ACCEPT_KEY   = "Accept";
    private final static String HEADER_ACCEPT_VALUE = "application/json";

    private final static String HEADER_XAPIKEY_KEY  = "x-api-key";

    private final static String PROPERTIES_NAME_API_KEY = "setlist_fm_api_key";

    private static String apiKey;

    private final static Map<String, String> requestHeaders = new HashMap<>();

    static {
        apiKey = Properties.getValue(PROPERTIES_NAME_API_KEY);
        System.out.println("Read property: PROPERTIES_NAME_API_KEY: " + apiKey);
        requestHeaders.put(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE);
        requestHeaders.put(HEADER_XAPIKEY_KEY, apiKey);
    }

    private final RestClient restClient;

    private SetlistFMClient() {
        restClient = new RestClient();
    }

    private static SetlistFMClient instance = new SetlistFMClient();

    public static SetlistFMClient getInstance() {
        return instance;
    }

    public UserAttendedResponse getConcertsAttendedByUser(String username) {

        UserAttendedResponse response = getConcertsAttendedByUser(username, 1);

        if (response != null) {
            int lastPage = (int) Math.ceil((double) response.total / (double) response.itemsPerPage);
            if (response.setlist == null) {
                response.setlist = new ArrayList<>();
            }

            for (int page = 2; page <= lastPage; page++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                UserAttendedResponse nextPageResponse = getConcertsAttendedByUser(username, page);

                if (nextPageResponse != null) {
                    if ((nextPageResponse.code < 400) &&
                        (nextPageResponse.setlist != null) && !nextPageResponse.setlist.isEmpty()) {

                        response.setlist.addAll(nextPageResponse.setlist);
                    } else {
                        System.out.println("code: " + nextPageResponse.code);
                        System.out.println("status: " + nextPageResponse.status);
                        System.out.println("message: " + nextPageResponse.message);
                    }
                }
            }
        }
        return response;
    }

    public UserAttendedResponse getConcertsAttendedByUser(String username, int page) {
        UserAttendedResponse result = null;
        String urlStr = "https://api.setlist.fm/rest/1.0/user/%s/attended?p=%d"
                .formatted(username, page);
        RestResponse restResponse = restClient.get(urlStr, requestHeaders);
        System.out.println("Request: GET " + urlStr);
//        System.out.println("Response: " + restResponse.getCode() + " " +
//                restResponse.getReason());
        if ((restResponse.getCode() == HTTP_OK) &&
                (restResponse.getBody() != null)) {

            Gson gson = new Gson();
            try {
                result = gson.fromJson(restResponse.getBody(), UserAttendedResponse.class);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return result;
    }

    /*
    GET /1.0/user/{userId}/attended
Accept: application/json

     */

    // curl -X GET --header 'Accept: application/json'
    //             --header 'x-api-key: ICGgj2-jAn-1rZICSI1OcxqssYF9sJSlyCfg'
    //             'https://api.setlist.fm/rest/1.0/user/bamjuggler/attended?p=1'

    // https://api.setlist.fm/rest/1.0/user/bamjuggler/attended?p=1

    /*
    {
  "type": "setlists",
  "itemsPerPage": 20,
  "page": 1,
  "total": 3782,
  "setlist": [
    {
      "id": "ba9a92a",
      "versionId": "g1bae6100",
      "eventDate": "04-08-2024",
      "lastUpdated": "2024-08-07T03:51:29.482+0000",
      "artist": {
        "mbid": "7f83b479-2030-47c5-937e-d4a8f142f1b1",
        "name": "Dweezil Zappa",
        "sortName": "Zappa, Dweezil",
        "disambiguation": "",
        "url": "https://www.setlist.fm/setlists/dweezil-zappa-13d6a17d.html"
      },
      "venue": {
        "id": "63d63667",
        "name": "The Warfield",
        "city": {
          "id": "5391959",
          "name": "San Francisco",
          "state": "California",
          "stateCode": "CA",
          "coords": {
            "lat": 37.775,
            "long": -122.419
          },
          "country": {
            "code": "US",
            "name": "United States"
          }
        },
        "url": "https://www.setlist.fm/venue/the-warfield-san-francisco-ca-usa-63d63667.html"
      },
      "tour": {
        "name": "ROX(POSTROPH)Y Tour 2024"
      },
      "sets": {
        "set": [
          {
            "song": [
              {
                "name": "Heavy Duty Judy",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Don't Eat the Yellow Snow",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Nanook Rubs It",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "St. Alfonzo's Pancake Breakfast",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Approximate",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                },
                "info": "Played 3x. Standard, “with the feet”, and “with the mouth.”"
              },
              {
                "name": "Inca Roads",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Pygmy Twylyte",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "The Idiot Bastard Son",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Dickie's Such an Asshole",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Echidna's Arf (Of You)",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Don't You Ever Wash That Thing?",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Punky's Whips",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Cosmik Debris",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Semi-Fraudulent/Direct-From-Hollywood Overture",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "A Pound For A Brown",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Cheepnis",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Hungry Freaks, Daddy / You're Probably Wondering Why I'm Here / Harry, You're a Beast / Oh No / Son of Orange County / The Orange County Lumber Truck / More Trouble Every Day",
                "info": "with “Two Tickets to Paradise,” “Paradise City,” and “Stayin’ Alive” teases."
              },
              {
                "name": "Sharleena",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              },
              {
                "name": "Zomby Woof",
                "cover": {
                  "mbid": "e20747e7-55a4-452e-8766-7b985585082d",
                  "name": "Frank Zappa",
                  "sortName": "Zappa, Frank",
                  "disambiguation": "US musician, composer, activist & filmmaker",
                  "url": "https://www.setlist.fm/setlists/frank-zappa-3d6b52b.html"
                }
              }
            ]
          }
        ]
      },
      "url": "https://www.setlist.fm/setlist/dweezil-zappa/2024/the-warfield-san-francisco-ca-ba9a92a.html"
    },
     */

}

