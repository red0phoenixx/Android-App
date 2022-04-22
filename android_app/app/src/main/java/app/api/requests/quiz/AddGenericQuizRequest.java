package app.api.requests.quiz;

import app.api.generics.ApiRequest;
import app.api.generics.RequestCallback;
import app.api.generics.RequestType;
import app.api.quiz.GenericQuiz;

import org.json.JSONException;
import org.json.JSONObject;

public class AddGenericQuizRequest extends ApiRequest {

    public AddGenericQuizRequest(String token, String quizKey, GenericQuiz quiz, RequestCallback callback){
        super();
        addHeader("token", token);

        setEndpoint("/quiz/generic/");
        setRequestType(RequestType.POST);

        setCallback(callback);

        JSONObject quizJson = new JSONObject();
        try {
            quizJson.put("question", quiz.getQuestion());
            quizJson.put("correct_answer", quiz.getCorrectAnswer());
            quizJson.put("wrong_answers", quiz.getWrongAnswers());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();

        try {
            json.put("quiz_key", quizKey);
            json.put("quiz", quizJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setBody(json.toString());
    }
}
