import adapter.VendorAiAdapter;
import adapter.VendorAiClient;
import builder.AiRequest;
import chain_of_responsibility.BlankQuestionHandler;
import chain_of_responsibility.QuestionHandler;
import chain_of_responsibility.QuestionLengthHandler;
import chain_of_responsibility.QuestionMarkHandler;
import command.Command;
import command.CommandQueue;
import command.GenerateAnswerCommand;
import decorator.LoggingAiModel;
import decorator.TimingAiModel;
import dependency_injection.AiModel;
import dependency_injection.AnswerService;
import dependency_injection.FakeAiModel;
import facade.AiAssistantFacade;
import factory.AiModelFactory;
import observer.AnswerCountingObserver;
import observer.AnswerLengthObserver;
import observer.AnswerLoggingObserver;
import observer.AnswerObserver;
import strategy.ConcisePromptStrategy;
import strategy.TeachingPromptStrategy;
import template_method.AnswerWorkflow;
import template_method.ConciseAnswerWorkflow;
import template_method.TeachingAnswerWorkflow;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AiModel model = demonstrateAdapter();
        demonstrateStrategy(model);
        AnswerService decoratedService = demonstrateDecorator();
        AiModelFactory factory = demonstrateFactory();
        AnswerService observedService = demonstrateObserver(factory);
        QuestionHandler validation = demonstrateChainOfResponsibility(observedService);
        demonstrateBuilder();
        demonstrateFacade(model, validation);
        demonstrateTemplateMethod(factory);
        demonstrateCommand(decoratedService);
    }

    private static AiModel demonstrateAdapter() {
        //        the main idea is, you can use any ai model to perform the answer service
        //        AiModel model = new FakeAiModel();
        VendorAiClient client = new VendorAiClient();
        AiModel model = new VendorAiAdapter(client);
        return model;
    }

    private static void demonstrateStrategy(AiModel model) {
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

    private static AnswerService demonstrateDecorator() {
        //decorator
        System.out.println("===============Decorator Pattern===============");
        AiModel originalModel = new FakeAiModel();
        AiModel timedModel  = new TimingAiModel(originalModel);
        AiModel loggedModel  = new LoggingAiModel(originalModel);

        AnswerService answerService = new AnswerService(timedModel, new TeachingPromptStrategy());
        System.out.println(
                answerService.answer("What is the Decorator pattern?")
        );
        AnswerService answerService2 = new AnswerService(loggedModel, new TeachingPromptStrategy());
        System.out.println(
                answerService2.answer("What is the Decorator pattern? (logged model)")
        );
        return answerService;
    }

    private static AiModelFactory demonstrateFactory() {
        //factory
        System.out.println("===============Factory Pattern===============");
        AiModelFactory  factory = new AiModelFactory();
        AiModel baseModel = factory.create("vendor");

        AiModel timedModel1 = new TimingAiModel(baseModel);
        AnswerService answerService1 = new AnswerService(timedModel1, new TeachingPromptStrategy());
        System.out.println(
                answerService1.answer("What is a factory?")
        );
        return factory;
    }

    private static AnswerService demonstrateObserver(AiModelFactory factory) {
        //Observer
        System.out.println("===============Observer Pattern===============");
        AnswerService answerService3 = new AnswerService(factory.create("fake"), new TeachingPromptStrategy());

        AnswerObserver logger = new AnswerLoggingObserver();
        AnswerObserver counter = new AnswerCountingObserver();
        AnswerObserver lengthObserver = new AnswerLengthObserver();

        answerService3.addObserver(logger);
        answerService3.addObserver(counter);
        answerService3.addObserver(lengthObserver);

        String answer = answerService3.answer("What is Observer?");
        System.out.println(answer);
        answerService3.removeObserver(logger);
        answerService3.answer("What is Strategy?");
        return answerService3;
    }

    private static QuestionHandler demonstrateChainOfResponsibility(AnswerService answerService3) {
        //chain of responsibility
        System.out.println("===============Chain of Responsibility Pattern===============");
        QuestionHandler validation = new BlankQuestionHandler();

        validation.setNext(new QuestionLengthHandler(200))
                  .setNext(new QuestionMarkHandler());
        String question2 = "What is Chain of Responsibility?";

        try {
            validation.handle(question2);
            String answer2 = answerService3.answer(question2);
            System.out.println(answer2);
        } catch (IllegalArgumentException exception) {
            System.out.println("Rejected: " + exception.getMessage());
        }
        return validation;
    }

    private static void demonstrateBuilder() {
        //builder
        System.out.println("===============Builder Pattern===============");
        AiRequest customRequest = AiRequest.builder("What is the builder pattern?")
                .temperature(0.2)
                .maxTokens(300)
                .build();
        AiRequest defaultRequest = AiRequest.builder(
                "What is dependency injection?"
        ).build();

        System.out.println("Custom request:");
        System.out.println("Custom question= " +customRequest.getQuestion());
        System.out.println("Custom temp= " +customRequest.getTemperature());
        System.out.println("Custom max token= " +customRequest.getMaxTokens());

        System.out.println("Default request:");
        System.out.println("Default question= " + defaultRequest.getQuestion());
        System.out.println("Default temp= " +defaultRequest.getTemperature());
        System.out.println("Default max token= " +defaultRequest.getMaxTokens());
    }

    private static void demonstrateFacade(AiModel model, QuestionHandler validation) {
        //facade
        System.out.println("===============Facade Pattern===============");
        AnswerService answerService4 = new AnswerService(
                model,
                new TeachingPromptStrategy()
        );
        answerService4.addObserver(new AnswerLoggingObserver());
        AiAssistantFacade assistant = new AiAssistantFacade(
                validation,
                answerService4
        );
        try {
            String answer4 = assistant.ask("What is the Facade pattern?");
            System.out.println(answer4);
        } catch (IllegalArgumentException exception) {
            System.out.println("Rejected: " + exception.getMessage());
        }
    }

    private static void demonstrateTemplateMethod(AiModelFactory factory) {
        //template method
        System.out.println("===============Template method Pattern===============");
        AiModel model2 = factory.create("fake");
        AnswerWorkflow teaching = new TeachingAnswerWorkflow(model2);
        AnswerWorkflow concise =  new ConciseAnswerWorkflow(model2);

        String question4 = "What is inheritance?";
        System.out.println(teaching.answer(question4));
        System.out.println();

        System.out.println(concise.answer(question4));
    }

    private static void demonstrateCommand(AnswerService answerService) {
        //command
        System.out.println("===============Command Pattern===============");
        Command firstCommand = new GenerateAnswerCommand(
                answerService,
                "What is dependency injection"
        );
        Command secondCommand = new GenerateAnswerCommand(
                answerService,
                "What is dependency injection?"
        );

        CommandQueue queue = new CommandQueue();
        queue.submit(firstCommand);
        queue.submit(secondCommand);

        System.out.println("Commands queued. No answers generated yet.");

        queue.runAll();
    }
}
