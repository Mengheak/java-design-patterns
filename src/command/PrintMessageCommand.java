package command;

public class PrintMessageCommand implements Command {
    private final String message;
    public PrintMessageCommand(String message) {
        this.message = message;
    }
    @Override
    public void execute() {
        System.out.println(message);
    }
}
