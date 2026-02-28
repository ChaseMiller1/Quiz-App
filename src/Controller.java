import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField numQuestions;
    @FXML private ComboBox<?> subjectBox;
    @FXML private ComboBox<?> typeBox;
    @FXML private ComboBox<?> difficultyBox;

    @FXML private TextArea questionTextArea;
    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;

    @FXML private TextField numRight;
    @FXML private TextField numWrong;
    @FXML private TextField score;

    @FXML private TextField timer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: Initialize your ComboBoxes, default values, and listeners here
    }

    @FXML
    private void buttonAPressed(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void buttonBPressed(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void buttonCPressed(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void buttonDPressed(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void getNumQuestions(MouseEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void loadQuiz(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void nextQuestion(ActionEvent event) {
        // TODO: Implement method
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

    @FXML
    private void setDefaults(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void setDifficulty(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void setSubject(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void setType(ActionEvent event) {
        // TODO: Implement method
    }

    @FXML
    private void startQuiz(ActionEvent event) {
        // TODO: Implement method
    }
}