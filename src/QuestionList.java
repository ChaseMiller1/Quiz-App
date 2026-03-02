import java.util.List;

public class QuestionList {
    List<Question> questions;
    int currentIndex;

    /**
     * 1-param constructor
     * @param questions for list
     */
    public QuestionList(List<Question> questions) {
        this.questions = questions;
        this.currentIndex = 0;
    }

    /**
     * Gets the next question in the list
     * @return next question
     */
    public Question nextQuestion() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            return questions.get(currentIndex);
        }
        return null;
    }

    public Question currentQuestion() {
        return questions.get(currentIndex);
    }

    public int size() {
        return questions.size();
    }

    /**
     * Serializes the questions in the list
     */
    public void serializeQuestions() {

    }
}
