package app.api.codes;

public enum ApiCodes {
    ERROR(-1),
    SUCCESS(0),
    BAD_USERNAME_OR_PASSWORD(1),
    ACCOUNT_ALREADY_EXISTS(2),
    NO_PASSWORD_OR_USERNAME_PROVIDED(3),
    MAX_CHAR_LIMIT_EXCEEDED(4), // you exceed the 20 chars limit on the username
    NO_TOKEN_PROVIDED(5),
    INVALID_QUIZ_REQUESTED(6),
    INVALID_QUIZ_KEY(7),
    NO_QUIZ_PROVIDED(8),
    NOT_IMPLEMENTED_YET(9)
    ;


    private final int code;

    ApiCodes(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ApiCodes getFromCode(int code){
        for (ApiCodes value: values()){
            if (value.getCode() == code) return value;
        }

        return ApiCodes.ERROR;
    }
}
