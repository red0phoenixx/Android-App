package app.api.requests.account;

import org.json.JSONException;
import org.json.JSONObject;

import app.api.generics.ApiRequest;
import app.api.generics.RequestCallback;
import app.api.generics.RequestType;

public class CreateAccountRequest extends ApiRequest {

    public CreateAccountRequest(String username, String password, RequestCallback callback){
        JSONObject object = new JSONObject();

        try {
            object.put("username", username);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.setBody(object.toString());
        super.setCallback(callback);
        super.setRequestType(RequestType.POST);
        super.setEndpoint("/accounts/create");
    }
}
