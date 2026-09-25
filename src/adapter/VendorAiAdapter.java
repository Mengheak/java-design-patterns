package adapter;

import dependency_injection.AiModel;

public class VendorAiAdapter implements AiModel {
    private final VendorAiClient client;
    public VendorAiAdapter(VendorAiClient client) {
        this.client = client;
    }
    @Override
    public String generate(String question) {
        return client.chat(question, 0.7);
    }
}
