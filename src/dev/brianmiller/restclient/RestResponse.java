package dev.brianmiller.restclient;

public class RestResponse {

    private final int code;
    private final String reason;
    private final String body;

    public RestResponse(int code, String reason, String body) {
        this.code = code;
        this.reason = reason;
        this.body = body;
    }

    public RestResponse(int code, String body) {
        this.code = code;
        this.reason = null;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
