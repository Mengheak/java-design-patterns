package state;

import dependency_injection.AnswerService;

public class ChatSession {
    private final AnswerService answerService;
    private ChatState state;

    public ChatSession(AnswerService answerService) {
        this.answerService = answerService;
        this.state = new ActiveChatState();
    }

    public String ask(String question) {
        return state.ask(this, question);
    }

    public void close() {
        state.close(this);
    }

    public void reopen() {
        state.reopen(this);
    }

    public String generateAnswer(String question) {
        return answerService.answer(question);
    }

    public void changeState(ChatState state) {
        this.state = state;
    }
}
