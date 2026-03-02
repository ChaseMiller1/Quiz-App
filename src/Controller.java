import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Slider questionBar;
    @FXML private TextField numQuestions;
    @FXML private ComboBox<String> subjectBox;
    @FXML private ComboBox<String> typeBox;
    @FXML private ComboBox<String> difficultyBox;

    @FXML private TextArea questionTextArea;
    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;
    @FXML private List<Button> buttons;
    @FXML private Button nextButton;
    @FXML private HBox CDHBox;

    @FXML private TextField numRight;
    @FXML private TextField numWrong;
    @FXML private TextField score;

    @FXML private Label timeLabel;
    @FXML private TextField timer;

    final QuizFetcher quizFetcher = new QuizFetcher();
    private QuestionList questions;
    private HashMap<String, Integer> subjects;
    private HashMap<String, String> types;
    private List<String> difficulties;

    private boolean quizStarted;
    private boolean answered;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            subjects = quizFetcher.getAllCategories();
            types = quizFetcher.getTypeOptions();
            difficulties = quizFetcher.getDifficultyOptions();

            subjectBox.setItems(FXCollections.observableArrayList(subjects.keySet()).sorted());
            typeBox.setItems(FXCollections.observableArrayList(types.keySet()).sorted());
            difficultyBox.setItems(FXCollections.observableArrayList(difficulties));

            subjectBox.getSelectionModel().selectFirst();
            typeBox.getSelectionModel().selectFirst();
            difficultyBox.getSelectionModel().selectFirst();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Issue fetching data, close and try again");
        }

        buttons = new ArrayList<>();
        buttons.add(buttonA);
        buttons.add(buttonB);
        buttons.add(buttonC);
        buttons.add(buttonD);
        quizStarted = false;
        answered = true;
    }

    @FXML
    private void getNumQuestions() {
        numQuestions.setText(String.valueOf((int) questionBar.getValue()));
    }

    @FXML
    private void setDefaults() {
        questionBar.setValue(5);
        numQuestions.setText("5");
        subjectBox.getSelectionModel().selectFirst();
        typeBox.getSelectionModel().selectFirst();
        difficultyBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void startQuiz() throws Exception {
        if (!quizStarted) {
            String type = types.get(typeBox.getValue());

            List<Question> fetched = quizFetcher.fetchQuestions(
                    (int) questionBar.getValue(),
                    subjects.get(subjectBox.getValue()),
                    difficultyBox.getValue(),
                    type
            );

            if (fetched != null && !fetched.isEmpty()) {
                questions = new QuestionList(fetched);
                quizStarted = true;
                answered = false;
                numRight.setText("0");
                numWrong.setText("0");
                score.setText("0%");
                updateQuestionUI(questions.currentQuestion());
            } else {
                new Alert(Alert.AlertType.WARNING, "No questions found for these settings.").showAndWait();
            }
        }
    }

    @FXML
    private void loadQuiz(ActionEvent event) {
        // TODO: Implement method
    }

    /**
     * Helper to refresh the UI with the current question.
     */
    private void updateQuestionUI(Question current) {
        if (current != null) {
            current.updateUI(questionTextArea, CDHBox, buttons);
        }
    }

    @FXML private void buttonAPressed() { buttonPressed(buttonA); }
    @FXML private void buttonBPressed() { buttonPressed(buttonB); }
    @FXML private void buttonCPressed() { buttonPressed(buttonC); }
    @FXML private void buttonDPressed() { buttonPressed(buttonD); }

    private void buttonPressed(Button button) {
        if (!answered) {
            answered = true;
            nextButton.setText("Next");
            int right = Integer.parseInt(numRight.getText());
            int wrong = Integer.parseInt(numWrong.getText());
            if (button.getText().equals(questions.currentQuestion().getCorrectAnswer())) {
                button.setStyle("-fx-background-color: #A9DFBF;");
                right++;
                numRight.setText(String.valueOf(right));
            } else {
                button.setStyle("-fx-background-color: #F1948A;");
                colorCorrectButton();
                wrong++;
                numWrong.setText(String.valueOf(wrong));
            }
            score.setText(100 * right / (right + wrong) + "%");
        }
    }

    @FXML
    private void nextQuestion() {
        if (quizStarted) {
            if (!answered) {
                colorCorrectButton();
                nextButton.setText("Next");
                answered = true;
            } else {
                resetColors();
                updateQuestionUI(questions.nextQuestion());
                nextButton.setText("Skip");
                answered = false;
            }
        }
    }

    private void colorCorrectButton() {
        for (Button button : buttons) {
            if (button.getText().equals(questions.currentQuestion().getCorrectAnswer())) {
                button.setStyle("-fx-background-color: #A9DFBF;");
            }
        }
    }

    private void resetColors() {
        for (Button button : buttons) {
            button.setStyle("");
        }
    }

    @FXML
    private void pauseQuiz(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void quitQuiz(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void saveQuiz(ActionEvent event) {
        // TODO: Implement method
    }
}