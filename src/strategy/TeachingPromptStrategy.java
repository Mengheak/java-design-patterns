package strategy;

public class TeachingPromptStrategy implements PromptStrategy {
    @Override
    public String prepare(String question) {
        return "Explain step by step for a beginner, with an example: "
                + question;
    }
}
