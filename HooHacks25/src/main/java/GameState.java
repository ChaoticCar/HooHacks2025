public interface GameState {

    public GameStateName getGameStateName();

    public void enterState();
    public void update();
    public GameState handleInput(GameInput input);
    public void exit();
}
