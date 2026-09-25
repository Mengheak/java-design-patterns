package observer;

public class AnswerCountingObserver implements AnswerObserver {

    private int count;

    @Override
    public void onAnswerGenerated(String question, String answer) {
        count++;
        System.out.println("[Count] Answers generated: " + count);
    }
    public int getCount() {
        return count;
    }
}
