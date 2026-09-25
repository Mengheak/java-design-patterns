package observer;

public class AnswerLoggingObserver implements AnswerObserver {
    @Override
    public void onAnswerGenerated(String question, String answer) {
        System.out.println("[Log] Answer generated for: " + question);
    }
}
