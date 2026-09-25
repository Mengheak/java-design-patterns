package dependency_injection;

import observer.AnswerObserver;
import strategy.PromptStrategy;

import java.util.ArrayList;
import java.util.List;

public class AnswerService {
 private final AiModel model;
 private final PromptStrategy promptStrategy;

 private final List<AnswerObserver> observers = new ArrayList<>();

    public AnswerService(AiModel model, PromptStrategy promptStrategy) {
        this.promptStrategy = promptStrategy;
        this.model = model;
    }
 public void addObserver(AnswerObserver observer) {
     observers.add(observer);
 }
    public void removeObserver(AnswerObserver observer) {
        observers.remove(observer);
    }

 public String answer(String question) {
     String prompt = promptStrategy.prepare(question);
     String answer = model.generate(prompt);

     for (AnswerObserver observer : observers) {
         observer.onAnswerGenerated(answer, prompt);
     }
     return answer;
 }
}