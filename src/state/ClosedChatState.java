package state;

public class ClosedChatState implements ChatState {
    @Override
    public String ask(ChatSession session, String question) {
        throw new IllegalStateException(
                "Cannot ask questions in a closed session"
        );
    }

    @Override
    public void close(ChatSession session) {
        System.out.println("Session is already closed.");
    }
}
