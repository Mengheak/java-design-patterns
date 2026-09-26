package command;

import dependency_injection.AnswerService;

public class GenerateAnswerCommand implements Command {
    private final AnswerService answerService;
    private final String question;
    public GenerateAnswerCommand(AnswerService answerService, String question) {
        this.answerService = answerService;
        this.question = question;
    }
    @Override
    public void execute() {
        String answer = answerService.answer(question);
        System.out.println(answer);
    }
}
