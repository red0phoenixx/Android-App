package de.uwuwhatsthis.quizApp.ui.loginScreen.api.quiz;

import de.uwuwhatsthis.quizApp.ui.loginScreen.api.codes.ApiCodes;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.codes.HttpCodes;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics.ApiRequest;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics.QuizCallback;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.requests.quiz.GetRandomGenericQuizRequest;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.state.StateManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenericQuiz {
    private int id;
    private String question;
    private String correctAnswer;

    private String[] wrongAnswers;

    public GenericQuiz(int id, String question, String correctAnswer, String[] wrongAnswers){
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    public static void getRandomQuiz(QuizCallback quizCallback){
        ApiRequest request = new GetRandomGenericQuizRequest(StateManager.getInstance().getToken(), response -> {
            if (!response.couldConnect() || response.getHttpCode() != HttpCodes.OK){
                System.err.println("Response is either null or does not have the 200 code!\nResponse: " + (response.getResponse() != null ? response.getResponse().code() : "null"));
                quizCallback.execute(null);
                return;
            }

            int code = response.getJson().optInt("code", -1);

            ApiCodes apiCode = ApiCodes.getFromCode(code);

            if (apiCode == null){
                System.err.println("Invalid api code after requesting generic Quiz: " + code);
                quizCallback.execute(null);
                return;
            }

            if (apiCode == ApiCodes.SUCCESS){
                JSONObject quizObject;

                quizObject = response.getJson().optJSONObject("quiz");


                int id;
                String question, correctAnswer;

                id = quizObject.optInt("id", -1);
                question = quizObject.optString("question");
                correctAnswer = quizObject.optString("correct_answer");

                JSONArray wrongAnswersPossible = quizObject.optJSONArray("possible_answers");

                String[] wrongAnswers = new String[wrongAnswersPossible.length()];

                for (int i = 0; i < wrongAnswersPossible.length(); i++){
                    wrongAnswers[i] = wrongAnswersPossible.optString(i, null);
                }

                GenericQuiz genericQuiz = new GenericQuiz(id, question, correctAnswer, wrongAnswers);

                quizCallback.execute(genericQuiz);
            }
        });

        request.doRequest();
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }
}
