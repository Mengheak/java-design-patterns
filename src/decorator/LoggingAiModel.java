package decorator;

import dependency_injection.AiModel;

public class LoggingAiModel implements AiModel {

    private final AiModel loggingModel;
    public LoggingAiModel(AiModel loggingModel) {
        this.loggingModel = loggingModel;
    }

    @Override
    public String generate(String question) {
       System.out.println("Calling model.....");
       return loggingModel.generate(question);
    }
}
