package facade;

import chain_of_responsibility.QuestionHandler;
import dependency_injection.AnswerService;

public class AiAssistantFacade {
    private final QuestionHandler validation;
    private final AnswerService answerService;

    public AiAssistantFacade(QuestionHandler validation, AnswerService answerService) {
        this.validation = validation;
        this.answerService = answerService;
    }

    public String ask(String question){
        validation.handle(question);
        return answerService.answer(question);
    }
}
