package state;

public interface ChatState {
    String ask(ChatSession session, String question);
    void close(ChatSession session);

    void reopen(ChatSession session);
}
