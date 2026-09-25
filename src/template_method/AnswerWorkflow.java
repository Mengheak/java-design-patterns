package template_method;

import dependency_injection.AiModel;

public abstract class AnswerWorkflow {
    private final AiModel model;
    public AnswerWorkflow(AiModel model) {
        this.model = model;
    }

    // The template method defines the fixed workflow.
    public final String answer(String question) {
        validate(question);

        String prompt = preparePrompt(question);
        String response = model.generate(prompt);

        return formatAnswer(response);
    }
    private void validate(String question) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Question must not be blank"
            );
        }
    }
    protected abstract String preparePrompt(String question);

    protected String formatAnswer(String response) {
        return response;
    }
}
