package app.ui;

import android.widget.Button;
import android.widget.TextView;

import app.MainActivity;
import de.uwuwhatsthis.quizApp.ui.loginScreen.R;
import app.api.quiz.GenericQuiz;
import app.ui.utils.Utils;

public class QuizScreen {
    private MainActivity instance;

    private TextView frage;

    private Button eins, zwei, drei, vier;

    public QuizScreen(){

        instance = MainActivity.getInstance();

        run();
    }

    private void run(){
        // Hier ist die Api-Anfrage in der GenericQuiz Klasse verpackt. Hier muss nur noch geprüft werden ob die Anfrage erfolgreich war.
        GenericQuiz.getRandomQuiz(quiz -> {
            if (quiz == null){
                Utils.showErrorMessage(instance, "Ein Fehler ist aufgetreten, als ein Quiz vom Server abgefraget werden sollte!", "");
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
