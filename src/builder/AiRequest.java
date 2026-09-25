package builder;

public class AiRequest {
    private final String question;
    private final double temperature;
    private final int maxTokens;

    private AiRequest(Builder builder) {
        this.question = builder.question;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
    }


    public String getQuestion() {
        return question;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public static Builder builder(String question) {
        return new Builder(question);
    }

    public static class Builder {
        private final String question;
        private double temperature = 0.7;
        private int maxTokens = 500;

        public Builder(String question) {
            this.question = question;
        }
        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public AiRequest build() {
            if (question == null || question.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Question must not be blank"
                );
            }

            if (!Double.isFinite(temperature) || temperature < 0) {
                throw new IllegalArgumentException(
                        "Temperature must be finite and non-negative"
                );
            }

            if (maxTokens <= 0) {
                throw new IllegalArgumentException(
                        "Max tokens must be positive"
                );
            }

            return new AiRequest(this);
        }

    }
}
