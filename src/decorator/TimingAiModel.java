package decorator;

import dependency_injection.AiModel;

public class TimingAiModel implements AiModel {
    private final AiModel wrappedModel;
    public TimingAiModel(AiModel wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    @Override
    public String generate(String question) {
        long start = System.nanoTime();
        try{
            return wrappedModel.generate(question);
        }finally{
            long elapsed = System.nanoTime() - start;
            double milliseconds = (double) elapsed / 1_000_000.0;
            System.out.printf("Model took %.2f ms%n", milliseconds);
        }
    }
}
