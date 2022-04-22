package de.uwuwhatsthis.quizApp.ui.loginScreen.api.codes;

public enum HttpCodes {
    ERROR(-1),
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    private final int code;

    HttpCodes(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HttpCodes getFromCode(int code){
        for (HttpCodes value: values()){
            if (value.getCode() == code) return value;
        }

        return HttpCodes.ERROR;
    }
}
