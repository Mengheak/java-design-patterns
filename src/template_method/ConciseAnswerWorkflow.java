package template_method;

import dependency_injection.AiModel;

public class ConciseAnswerWorkflow extends AnswerWorkflow {
    public ConciseAnswerWorkflow(AiModel model) {
        super(model);
    }
    @Override
    protected String preparePrompt(String question) {
        return "Answer in one sentence: " + question;
    }
}
