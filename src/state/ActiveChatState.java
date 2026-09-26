package state;

public class ActiveChatState implements ChatState {

    @Override
    public String ask(ChatSession session, String question) {
        return session.generateAnswer(question);
    }

    @Override
    public void close(ChatSession session) {
        session.changeState(new ClosedChatState());
        System.out.println("Session closed.");
    }

    @Override
    public void reopen(ChatSession session) {
        System.out.println("Session is already active.");
    }
}
