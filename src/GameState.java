public interface GameState {

    public GameStateName getGameStateName();

    public void enterState();
    public void update();
    public void handleInput(GameInput input);
    public void exit();
}
