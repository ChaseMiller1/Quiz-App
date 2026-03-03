import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for Quiz App UI
 */
public class Controller implements Initializable {

    private final String STYLE_CORRECT = "CORRECT";
    private final String STYLE_WRONG = "WRONG";
    private final String STYLE_RESET = "RESET";

    @FXML private Slider questionBar;
    @FXML private TextField numQuestions;
    @FXML private ComboBox<String> subjectBox;
    @FXML private ComboBox<String> typeBox;
    @FXML private ComboBox<String> difficultyBox;

    @FXML private TextArea questionTextArea;
    @FXML private ProgressBar quizProgress;

    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;
    private List<Button> buttons;

    @FXML private Button nextButton;
    @FXML private Button fileButton;
    @FXML private HBox CDHBox;

    @FXML private TextField numRight;
    @FXML private TextField numWrong;
    @FXML private TextField score;

    @FXML private Label timeLabel;
    @FXML private TextField timer;
    @FXML private Button pauseButton;
    private Timeline quizTimeline;
    private int secondsElapsed;

    final QuizFetcher quizFetcher = new QuizFetcher();
    private QuestionList questions;
    private HashMap<String, Integer> subjects;
    private HashMap<String, String> types;

    private boolean quizStarted;
    private boolean answered;
    private boolean paused;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            subjects = quizFetcher.getAllCategories();
            types = quizFetcher.getTypeOptions();

            subjectBox.setItems(FXCollections.observableArrayList(subjects.keySet()).sorted());
            typeBox.setItems(FXCollections.observableArrayList(types.keySet()).sorted());
            difficultyBox.setItems(FXCollections.observableArrayList(quizFetcher.getDifficultyOptions()));

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
        paused = false;
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
                initializeQuizUI();
            } else {
                new Alert(Alert.AlertType.WARNING, "No questions found for these settings.").showAndWait();
            }
        }
    }

    @FXML
    private void quizLoading() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text files (*.txt)", "*.txt"));

        if (quizStarted) {
            if (!paused) {
                pauseQuiz();
            }

            fileChooser.setTitle("Save Quiz");
            fileChooser.setInitialFileName("my_quiz.txt");
            File file = fileChooser.showSaveDialog(nextButton.getScene().getWindow());

            if (file != null && questions != null) {
                questions.serializeQuestions(file);
            }
        } else {
            fileChooser.setTitle("Load Quiz");
            File file = fileChooser.showOpenDialog(nextButton.getScene().getWindow());

            if (file != null) {
                List<Question> loaded = FileUtilities.loadQuestions(file);
                if (loaded != null) {
                    questions = new QuestionList(loaded);
                    initializeQuizUI();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Could not load the quiz file.").show();
                }
            }
        }
    }

    @FXML
    private void nextQuestion() {
        if (quizStarted && !paused) {
            if (!answered) {
                numWrong.setText(String.valueOf(Integer.parseInt(numWrong.getText()) + 1));
                updateScoreDisplay();
                colorCorrectButton();
                nextButton.setText("Next");
                answered = true;
            } else {
                resetColors();
                updateQuestionUI(questions.nextQuestion());
                nextButton.setText("Skip");
                answered = false;

                int right = Integer.parseInt(numRight.getText());
                int wrong = Integer.parseInt(numWrong.getText());
                quizProgress.setProgress((double) (right + wrong) / questions.size());
            }
        }
    }

    /**
     * Checks to see if the pressed button is the correct answer
     * @param button that was pressed
     */
    private void buttonPressed(Button button) {
        if (!answered) {
            answered = true;
            nextButton.setText("Next");
            int right = Integer.parseInt(numRight.getText());
            int wrong = Integer.parseInt(numWrong.getText());

            if (button.getText().equals(questions.currentQuestion().getCorrectAnswer())) {
                setUIStyle(button, STYLE_CORRECT);
                right++;
                numRight.setText(String.valueOf(right));
            } else {
                setUIStyle(button, STYLE_WRONG);
                colorCorrectButton();
                wrong++;
                numWrong.setText(String.valueOf(wrong));
            }
            updateScoreDisplay();
        }
    }

    @FXML private void buttonAPressed() { buttonPressed(buttonA); }
    @FXML private void buttonBPressed() { buttonPressed(buttonB); }
    @FXML private void buttonCPressed() { buttonPressed(buttonC); }
    @FXML private void buttonDPressed() { buttonPressed(buttonD); }

    @FXML
    private void pauseQuiz() {
        if (quizStarted) {
            if (!paused) {
                quizTimeline.pause();
                resetQuestionUI();
                pauseButton.setText("Unpause");
                paused = true;
            } else {
                quizTimeline.play();
                updateQuestionUI(questions.currentQuestion());
                pauseButton.setText("Pause");
                paused = false;
            }
        }
    }

    @FXML
    private void quitQuiz() {
        if (quizStarted) {
            updateQuestionUI(null);
        }
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

    private void initializeQuizUI() {
        setUIStyle(score, STYLE_RESET);
        setUIStyle(timer, STYLE_RESET);
        resetColors();

        timeLabel.setText("Time:");
        quizProgress.setProgress(0.0);
        numRight.setText("0");
        numWrong.setText("0");
        score.setText("0%");

        quizStarted = true;
        answered = false;
        fileButton.setText("Save Quiz");

        startTimer();
        updateQuestionUI(questions.currentQuestion());
    }

    /**
     * Scoring system for calculating and displaying current score
     */
    private void updateScoreDisplay() {
        int right = Integer.parseInt(numRight.getText());
        int wrong = Integer.parseInt(numWrong.getText());
        int total = right + wrong;

        if (total == 0) {
            score.setText("0%");
        } else {
            score.setText((100 * right / total) + "%");
        }
    }

    /**
     * Helper to refresh the UI with the current question.
     */
    private void updateQuestionUI(Question current) {
        if (current != null) {
            current.updateUI(questionTextArea, CDHBox, buttons);
        } else {
            quizStarted = false;
            answered = true;
            paused = false;

            resetQuestionUI();
            pauseButton.setText("Pause");
            fileButton.setText("Load Quiz");
            timeLabel.setText("Final Time:");

            if (quizTimeline != null) {
                quizTimeline.stop();
            }

            setUIStyle(timer, STYLE_CORRECT);
            setUIStyle(score, STYLE_CORRECT);
        }
    }

    /**
     * Starts quiz timer
     */
    private void startTimer() {
        secondsElapsed = 0;
        timer.setText("00:00");
        if (quizTimeline != null) {
            quizTimeline.stop();
        }

        quizTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            timer.setText(String.format("%02d:%02d", minutes, seconds));
        }));

        quizTimeline.setCycleCount(Animation.INDEFINITE);
        quizTimeline.play();
    }

    /**
     * Colors correct answer
     */
    private void colorCorrectButton() {
        for (Button button : buttons) {
            if (button.getText().equals(questions.currentQuestion().getCorrectAnswer())) {
                setUIStyle(button, "CORRECT");
            }
        }
    }

    /**
     * Resets button colors
     */
    private void resetColors() {
        for (Button button : buttons) {
            setUIStyle(button, "RESET");
        }
    }

    /**
     * Resets question UI for finishing and pausing quiz
     */
    private void resetQuestionUI() {
        questionTextArea.setText("");
        buttonA.setText("Button A");
        buttonB.setText("Button B");
        buttonC.setText("Button C");
        buttonD.setText("Button D");
    }

    /**
     * Sets color of element based on its state
     * @param element to color
     * @param state of element
     */
    private void setUIStyle(Control element, String state) {
        switch(state) {
            case STYLE_CORRECT: element.setStyle("-fx-background-color: #A9DFBF;"); break;
            case STYLE_WRONG:   element.setStyle("-fx-background-color: #F1948A;"); break;
            case STYLE_RESET:   element.setStyle(""); break;
        }
    }
}