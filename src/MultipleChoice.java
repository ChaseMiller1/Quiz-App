import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import java.util.List;


/**
 * Multiple choice question
 */
public class MultipleChoice extends Question {

    @Override
    public void updateUI(TextArea textArea, HBox CDBox, List<Button> buttons) {
        textArea.setText(this.getQuestion());
        CDBox.setVisible(true);
        List<String> answers = getShuffledAnswers();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(answers.get(i));
        }
    }
}