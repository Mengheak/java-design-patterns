package factory;

import adapter.VendorAiAdapter;
import adapter.VendorAiClient;
import dependency_injection.AiModel;
import dependency_injection.FakeAiModel;

public class AiModelFactory {
    public AiModel create(String type){
        if(type == null){
            throw new IllegalArgumentException("Model type is required");
        }
        switch (type){
            case "fake":
                return new FakeAiModel();

            case "vendor":
                VendorAiClient client = new VendorAiClient();
                return new VendorAiAdapter(client);

            default:
                throw new IllegalArgumentException(
                        "Unknown model type: " + type
                );
        }
    }
}
