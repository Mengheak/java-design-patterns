package dependency_injection;
public class FakeAiModel implements AiModel {
    @Override
    public String generate(String question) {
        return "Fake answer to: " + question;
    }
}
