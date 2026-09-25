package chain_of_responsibility;

public abstract class QuestionHandler {
    private QuestionHandler next;
    public QuestionHandler setNext(QuestionHandler next) {
        this.next = next;
        return next;
    }

    public void handle(String question) {
        check(question);

        if (next != null) {
            next.handle(question);
        }
    }
    protected abstract void check(String question);
}
