import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * True or false question
 */
public class TrueFalse extends Question {

    @Override
    public void updateUI(TextArea textArea, HBox CDBox, List<Button> buttons) {
        textArea.setText(this.getQuestion());
        CDBox.setVisible(false);
        buttons.getFirst().setText("True");
        buttons.get(1).setText("False");
    }
}