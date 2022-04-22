import api.codes.ApiCodes;
import api.codes.HttpCodes;
import api.quiz.GenericQuiz;
import api.requests.account.LoginRequest;
import api.requests.quiz.AddGenericQuizRequest;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        QuizFileParser parser = new QuizFileParser("/run/media/ole/WEEE/Schule/p-seminar/QuizFileParser/src/main/java/test.txt");

        List<GenericQuiz> quizList;

        try {
             quizList = parser.parse();
        } catch (IOException e) {
            System.err.println("Datei nicht gefunden!");
            e.printStackTrace();
            return;
        }

        System.out.println("Quizze in der Datei: " + quizList.toString());

        // um Quizzes zur Datenbank hinzuzufügen, muss man sich erstmal einloggen, da man den Token braucht
        LoginRequest loginRequest = new LoginRequest("lol", "123", loginResponse -> {
            if (!loginResponse.couldConnect()){
                System.err.println("Verbindung zum Server ist Fehlgeschlagen!");
                return;
            }

            if (loginResponse.getHttpCode() != HttpCodes.OK){
                System.err.println("Etwas ist beim verbinden zum Server schiefgelaufen!");
                System.err.println("HttpCode: " + (loginResponse.getHttpCode() == HttpCodes.ERROR ? loginResponse.getResponse().code() : loginResponse.getHttpCode()));
                return;
            }

            if (loginResponse.getApiCode() != ApiCodes.SUCCESS){
                System.err.println("Etwas ist beim Anmelden schiefgelaufen!");
                System.err.println("Api code: " + (loginResponse.getApiCode() == ApiCodes.ERROR ? loginResponse.getJson().opt("code") : loginResponse.getApiCode()));
                return;
            }

            String token = loginResponse.getJson().optString("token");

            System.out.println("Token vom server: " + token);

            // hier Quiz-key einfügen
            String quizKey = "xx";


            for (GenericQuiz quiz: quizList){
                AddGenericQuizRequest addQuizRequest = new AddGenericQuizRequest(token, quizKey, quiz, addQuizResponse -> {
                    if (!addQuizResponse.couldConnect()){
                        System.err.println("Konnte das Quiz mit Frage \"" + quiz.getQuestion() + "\" nicht hinzufügen, da die Verbindung zum Server fehlgeschlagen ist!");
                        return;
                    }

                    if (addQuizResponse.getHttpCode() != HttpCodes.OK){
                        System.err.println("Konnte das Quiz mit Frage \"" + quiz.getQuestion() + "\"  nicht hinzufügen, da etwas beim verbinden zum Server schiefgelaufen ist!");
                        System.err.println("HttpCode: " + (addQuizResponse.getHttpCode() == HttpCodes.ERROR ? addQuizResponse.getResponse().code() : addQuizResponse.getHttpCode()));
                        return;
                    }

                    if (addQuizResponse.getApiCode() != ApiCodes.SUCCESS){
                        System.err.println("Konnte das Quiz mit Frage \"" + quiz.getQuestion() + "\"  nicht hinzufügen!");
                        System.err.println("Api code: " + (addQuizResponse.getApiCode() == ApiCodes.ERROR ? addQuizResponse.getJson().opt("code") : addQuizResponse.getApiCode()));
                        return;

                    } else if (addQuizResponse.getApiCode() == ApiCodes.SUCCESS){
                        System.out.println("Quiz mit Frage: \"" + quiz.getQuestion() + "\" erfolgreich hinzugefügt!");

                    }
                });

                addQuizRequest.run();
            }

        });

        loginRequest.run();
    }
}
