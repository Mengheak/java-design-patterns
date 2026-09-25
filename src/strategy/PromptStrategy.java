package strategy;

//define the interchangeable behavior
public interface PromptStrategy {
    String prepare(String question);
}
