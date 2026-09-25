package chain_of_responsibility;

public class QuestionLengthHandler extends QuestionHandler {

    private final int maxLength;

    public QuestionLengthHandler(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void check(String question) {
        if (question.length() > maxLength) {
            throw new IllegalArgumentException(
                    "Question must not exceed "
                            + maxLength + " characters"
            );
        }
    }
}
