package de.uwuwhatsthis.quizApp.ui.loginScreen.api.requests.quiz;

import java.util.HashMap;

import de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics.ApiRequest;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics.RequestCallback;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics.RequestType;

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
