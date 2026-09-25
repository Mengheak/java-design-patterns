import adapter.VendorAiAdapter;
import adapter.VendorAiClient;
import decorator.TimingAiModel;
import dependency_injection.AiModel;
import dependency_injection.AnswerService;
import dependency_injection.FakeAiModel;
import factory.AiModelFactory;
import strategy.ConcisePromptStrategy;
import strategy.TeachingPromptStrategy;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //        the main idea is, you can use any ai model to perform the answer service
        //        AiModel model = new FakeAiModel();
        VendorAiClient client = new VendorAiClient();
        AiModel model = new VendorAiAdapter(client);


        //        Strategy lets you switch between different ways of performing the same task. In an AI app,
        //        you might use different strategies to prepare a prompt: one asks for a short answer,
        //        another asks for a beginner-friendly explanation.
        AnswerService conciseService = new AnswerService(
                model,
                new ConcisePromptStrategy()
        );
        AnswerService teachingService = new AnswerService(
                model,
                new TeachingPromptStrategy()
        );

        String question = "What is dependency injection?";

        System.out.println(conciseService.answer(question));
        System.out.println(teachingService.answer(question));


        //decorator
        System.out.println("===============Decorator Pattern===============");
        AiModel originalModel = new FakeAiModel();
        AiModel timedModel  = new TimingAiModel(originalModel);

        AnswerService answerService = new AnswerService(timedModel, new TeachingPromptStrategy());
        System.out.println(
                answerService.answer("What is the Decorator pattern?")
        );


        //factory
        System.out.println("===============Factory Pattern===============");
        AiModelFactory  factory = new AiModelFactory();
        AiModel baseModel = factory.create("vendor");

        AiModel timedModel1 = new TimingAiModel(baseModel);
        AnswerService answerService1 = new AnswerService(timedModel1, new TeachingPromptStrategy());
        System.out.println(
                answerService1.answer("What is a factory?")
        );
    }
}