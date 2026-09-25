package chain_of_responsibility;

public class BlankQuestionHandler extends QuestionHandler {

    @Override
    protected void check(String question) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Question must not be blank"
            );
        }
    }
}
