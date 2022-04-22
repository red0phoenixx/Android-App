package de.uwuwhatsthis.quizApp.ui.loginScreen.ui;

import android.widget.Button;
import android.widget.TextView;

import de.uwuwhatsthis.quizApp.ui.loginScreen.MainActivity;
import de.uwuwhatsthis.quizApp.ui.loginScreen.R;
import de.uwuwhatsthis.quizApp.ui.loginScreen.api.quiz.GenericQuiz;
import de.uwuwhatsthis.quizApp.ui.loginScreen.ui.utils.Utils;

public class QuizScreen {
    private MainActivity instance;

    private TextView frage;

    private Button eins, zwei, drei, vier;

    public QuizScreen(){

        instance = MainActivity.getInstance();

        run();
    }

    private void run(){
        GenericQuiz.getRandomQuiz(quiz -> {
            if (quiz == null){
                Utils.showErrorMessage(instance, "Failed to get quiz!", "");
                return;
            }

            instance.runOnUiThread(() -> {
                frage = instance.findViewById(R.id.tobi_frage);

                eins = instance.findViewById(R.id.tobi_button_eins);
                zwei = instance.findViewById(R.id.tobi_button_zwei);
                drei = instance.findViewById(R.id.tobi_button_drei);
                vier = instance.findViewById(R.id.tobi_button_vier);

                frage.setText(quiz.getQuestion());

                eins.setText(quiz.getWrongAnswers()[0]);
                zwei.setText(quiz.getWrongAnswers()[1]);
                drei.setText(quiz.getWrongAnswers()[2]);
                vier.setText(quiz.getCorrectAnswer());
            });
        });
    }


}
