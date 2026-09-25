import dependency_injection.AiModel;
import dependency_injection.AnswerService;
import dependency_injection.FakeAiModel;
import strategy.ConcisePromptStrategy;
import strategy.TeachingPromptStrategy;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //        the main idea is, you can use any ai model to perform the answer service
        AiModel model = new FakeAiModel();



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
    }
}