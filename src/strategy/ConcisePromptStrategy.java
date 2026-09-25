package strategy;

public class ConcisePromptStrategy implements PromptStrategy {

    @Override
    public String prepare(String question) {
        return "Answer in one sentence: " + question;
    }
}
