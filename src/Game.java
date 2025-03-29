//package src;

import java.util.ArrayList;

public class Game {

    private GameState gameState;

    private ArrayList<Sprite> sprites;

    private Player player;

    public Game () {
        sprites = new ArrayList<>();
        gameState = new OverworldState(this);

        Sprite playerSprite = new Sprite("images\\player.png", player);

        sprites.add(playerSprite);

    }

    public Player getPlayer() {
        return player;
    }
    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void handleInput(GameInput input) {

        // TODO: replace with game logic to decide if player moves
        gameState.handleInput(input);
        /*switch (gameState.getGameStateName()) {
            case GameStateName.OVERWORLD: {
                if (true) {
                    if (dirDown) {

                        //player.moveDown();
                    }
                }
                break;
            } case GameStateName.COMBAT: {

                break;
            } default: {
                System.out.println("game state switch is incomplete!");
            }
        }*/
    }

    public GameStateName getGameStateName() {
        return gameState.getGameStateName();
    }
}
