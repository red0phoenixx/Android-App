package app.api.generics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.api.codes.ApiCodes;
import app.api.codes.HttpCodes;
import okhttp3.Response;

public class ApiResponse {
    private final Response response;

    private ApiCodes apiCode;

    private final HttpCodes httpCode;

    private JSONObject json;

    private final boolean couldConnect;

    public ApiResponse(Response response){
        this.response = response;

        if (this.response == null){
            apiCode = ApiCodes.ERROR;
            httpCode = HttpCodes.ERROR;
            json = null;
            this.couldConnect = false;
            return;
        }

        couldConnect = true;

        httpCode = HttpCodes.getFromCode(response.code());

        try {
            json = new JSONObject(response.body().string());
        } catch (JSONException | IOException e) {
            json = null;
            return;
        }

        apiCode = ApiCodes.getFromCode(json.optInt("code", -1));
    }


    public Response getResponse() {
        return response;
    }

    public ApiCodes getApiCode() {
        return apiCode;
    }

    public HttpCodes getHttpCode() {
        return httpCode;
    }

    public JSONObject getJson() {
        return json;
    }

    public boolean couldConnect() {
        return couldConnect;
    }
}
