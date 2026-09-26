package command;

import java.util.ArrayDeque;
import java.util.Queue;

public class CommandQueue {
    private final Queue<Command> commands = new ArrayDeque<>();

    public void submit(Command command) {
        commands.add(command);
    }

    public void runAll() {
        while (!commands.isEmpty()) {
            Command command = commands.remove();
            command.execute();
        }
    }
}
