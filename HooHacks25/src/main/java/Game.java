//package src;

import java.util.ArrayList;

public class Game {

    private LLMInterface llmInterface;

    private GameState gameState;

    private ArrayList<Sprite> sprites;

    private Player player;
    private ArrayList<Hostile> hostiles;

    public Game (LLMInterface llmInterface) {

        this.llmInterface = llmInterface;

        sprites = new ArrayList<>();

        player = new Player();
        hostiles = new ArrayList<>();
        hostiles.add(new Hostile(5, "Giant Spider", 5, 1, 1, 10, 0));

        Sprite playerSprite = new Sprite(".\\HooHacks25\\images\\player.png", player);

        sprites.add(playerSprite);

        gameState = new OverworldState(this);

    }

    public Player getPlayer() {
        return player;
    }
    public ArrayList<Hostile> getHostiles() { return hostiles; }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void handleInput(GameInput input) {

        // TODO: replace with game logic to decide if player moves
        GameState nextState = gameState.handleInput(input);
        if (nextState != null) {
            gameState.exit();
            nextState.enterState();
            gameState = nextState;

        }
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

    public LLMInterface getLLMInterface() {
        return llmInterface;
    }
}
