package src;

public class Game {

    private Sprite[] sprites;

    private Player player;

    public Game () {
        player = new Player();

    }

    public void handleInput(boolean dirUp, boolean dirDown, boolean dirRight, boolean dirLeft) {

        // TODO: replace with game logic to decide if player moves
        if (true) {
            if (dirDown) {
                player.moveDown();
            }
        }
    }
}
