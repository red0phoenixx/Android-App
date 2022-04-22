package api.requests.quiz;

import java.util.HashMap;

import api.generics.ApiRequest;
import api.generics.RequestCallback;
import api.generics.RequestType;

public class GetRandomGenericQuizRequest extends ApiRequest {

    public GetRandomGenericQuizRequest(String token, RequestCallback callback){
        super();

        super.setHeaderMap(new HashMap<String, String>(){{
            put("token", token);
        }});

        super.setEndpoint("/quiz/generic/");
        super.setRequestType(RequestType.GET);
        super.setCallback(callback);

    }
}
