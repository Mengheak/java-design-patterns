package dependency_injection;

import strategy.PromptStrategy;

public class AnswerService {
 private final AiModel model;
 private final PromptStrategy promptStrategy;
 public AnswerService(AiModel model, PromptStrategy promptStrategy) {
     this.promptStrategy = promptStrategy;
     this.model = model;
 }
 public String answer(String question) {
     String prompt = promptStrategy.prepare(question);
     return this.model.generate(prompt);
 }
}