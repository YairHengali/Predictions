package engine.action.api;
public interface Action {
    void Run() throws Exception;
    ActionType getActionType();
}
