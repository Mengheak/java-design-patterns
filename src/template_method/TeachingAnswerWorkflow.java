package template_method;

import dependency_injection.AiModel;

public class TeachingAnswerWorkflow extends AnswerWorkflow {
    public TeachingAnswerWorkflow(AiModel model) {
        super(model);
    }

    @Override
    protected String preparePrompt(String question) {
        return "Explain step by step with an example: " + question;
    }

    @Override
    protected String formatAnswer(String response) {
        return "[Teaching answer]\n" + response;
    }
}
