import java.util.ArrayList;

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
    public GameState handleInput(GameInput input) {

        // TODO: replace with game logic to decide if player moves
        if (true) {
            switch (input) {
                case UP: {
                }
                case DOWN: {
                    // player.moveDown
                }
                case LEFT: {
                    player.setX(player.getX() - 1);
                }
                case RIGHT: {
                    player.setX(player.getX() + 1);

                }
                case BUTTON_A: {
                }
                case BUTTON_B: {
                } break;
                case BUTTON_X: {
                    ArrayList<CombatEntity> playerSide = new ArrayList<>();
                    playerSide.add(player);

                    ArrayList<CombatEntity> enemySide = new ArrayList<>(game.getHostiles());
                    System.out.println("Entering combat phase");

                    return new CombatState(game, playerSide, enemySide);
                } case BUTTON_Y: {

                }
            }
        }

        return null;
    }

    @Override
    public void exit() {

    }
}
