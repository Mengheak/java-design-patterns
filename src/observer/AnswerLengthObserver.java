package observer;

public class AnswerLengthObserver implements AnswerObserver{
    @Override
    public void onAnswerGenerated(String question, String answer) {
        System.out.println("[length] The answer length is: " + answer.length());
    }
}
