import javafx.scene.control.Alert;

import java.io.*;
import java.util.List;

public class FileUtilities {

    /**
     * Saves the list of questions to the specific file chosen by the user.
     * @param file to save to
     * @param questions to save
     */
    public static void saveQuestions(File file, List<Question> questions) {
        if (file != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(questions);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Questions could not be saved");
            }
        }
    }

    /**
     * Loads the list of questions from the specific file chosen by the user.
     * @param file to load
     */
    public static List<Question> loadQuestions(File file) {
        if (file != null && file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Question>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Questions could not be loaded");
                return null;
            }
        }
        return null;
    }
}
