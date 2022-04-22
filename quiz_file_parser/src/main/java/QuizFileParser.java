import api.quiz.GenericQuiz;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuizFileParser {

    private final String filePath, delimiter, newQuizDelimiter;

    private int questionIndex, correctAnswerIndex, wrongAnswerStartIndex;

    public QuizFileParser(String filePath){
        this(filePath, ";");
    }

    public QuizFileParser(String filePath, String delimiter){
        this(filePath, delimiter, "\n");
    }

    public QuizFileParser(String filePath, String delimiter, String newQuizDelimiter){
        this.filePath = filePath;
        this.delimiter = delimiter;
        this.newQuizDelimiter = newQuizDelimiter;

        this.questionIndex = 0;
        this.correctAnswerIndex = 1;
        this.wrongAnswerStartIndex = 2;
    }

    public List<GenericQuiz> parse() throws IOException {
        String content = Files.readString(Path.of(this.filePath), Charset.defaultCharset());

        LinkedList<GenericQuiz> quizList = new LinkedList<>();

        String[] quizzes = content.split(newQuizDelimiter);

        for (String quiz: quizzes){
            String[] options = quiz.split(delimiter);

            String question = options[questionIndex];
            String correctAnswer = options[correctAnswerIndex];

            String[] wrongAnswers = new String[3];

            int count = 0;

            for (int i = wrongAnswerStartIndex; i < wrongAnswerStartIndex + 3; i++){
                wrongAnswers[count] = options[i];
                count += 1;
            }

            quizList.add(new GenericQuiz(0, question, correctAnswer, wrongAnswers));
        }

        return quizList;
    }

    public void setQuestionIndex(int index){
        this.questionIndex = index;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setWrongAnswerStartIndex(int wrongAnswerStartIndex) {
        this.wrongAnswerStartIndex = wrongAnswerStartIndex;
    }
}
