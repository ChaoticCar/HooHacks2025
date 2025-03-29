//package src;

import java.util.ArrayList;

public class Game {

    private ArrayList<Sprite> sprites;

    private Player player;

    public Game () {
        player = new Player();
        sprites = new ArrayList<>();

        Sprite playerSprite = new Sprite("images\\player.png", player);

        sprites.add(playerSprite);

    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void handleInput(boolean dirUp, boolean dirDown, boolean dirRight, boolean dirLeft) {

        // TODO: replace with game logic to decide if player moves
        if (true) {
            if (dirDown) {
                //player.moveDown();
            }
        }
    }
}
