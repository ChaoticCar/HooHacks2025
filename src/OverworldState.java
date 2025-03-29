public class OverworldState implements GameState {

    Game game;
    Player player;

    OverworldState (Game game) {
        this.game = game;
        player = game.getPlayer();
    }

    @Override
    public GameStateName getGameStateName() {
        return GameStateName.OVERWORLD;
    }

    @Override
    public void enterState() {

    }

    @Override
    public void update() {

    }

    @Override
    public void handleInput(GameInput input) {

        // TODO: replace with game logic to decide if player moves
        if (true) {
            switch (input) {
                case UP -> {
                }
                case DOWN -> {
                    // player.moveDown
                }
                case LEFT -> {
                    player.setX(player.getX() - 1);
                }
                case RIGHT -> {
                    player.setX(player.getX() + 1);

                }
                case BUTTON_A -> {
                }
                case BUTTON_B -> {
                }
            }
                //player.moveDown();
        }
    }

    @Override
    public void exit() {

    }
}
