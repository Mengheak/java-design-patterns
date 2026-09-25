package chain_of_responsibility;

public class QuestionMarkHandler extends QuestionHandler {
    @Override
    protected void check(String question) {
        if(!question.endsWith("?")){
            throw new IllegalArgumentException("Question must end with '?'");
        }
    }
}
