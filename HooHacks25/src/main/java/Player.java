//package src;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;



public class Player implements Entity, CombatEntity {

    // image that represents the player's position on the board
    private BufferedImage image;
    // current position of the player on the board grid
    //private Point pos;
    private int posX;
    private int posY;
    // keep track of the player's score
    private int score;

    private ArrayList<Item> inventory = new ArrayList();

    // stats
    private int health = 5;
    private int maxHealth = 5;
    private int strength = 1;
    private int defense = 1;

    public Player() {
        // load the assets
        //loadImage();

        // initialize the state
        posX = 0;
        posY = 0;
        score = 0;
        inventory.add(Item.SWORD);
    }

    public boolean isPlayer() {
        return true;
    }
    public int getX() {
        return posX;
    }
    public int getY() {
        return posY;
    }

    public void setX(int x) { posX = x; }


    /*
    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("java_2d_game\\images\\player.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
            image, 
            pos.x * Screen.TILE_SIZE,
            pos.y * Screen.TILE_SIZE,
            observer
        );
    }
    
    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();
        
        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == KeyEvent.VK_UP) {
            pos.translate(0, -1);
        }
        if (key == KeyEvent.VK_RIGHT) {
            pos.translate(1, 0);
        }
        if (key == KeyEvent.VK_DOWN) {
            pos.translate(0, 1);
        }
        if (key == KeyEvent.VK_LEFT) {
            pos.translate(-1, 0);
        }
    }

    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the board sideways
        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x >= Screen.COLUMNS) {
            pos.x = Screen.COLUMNS - 1;
        }
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y >= Screen.ROWS) {
            pos.y = Screen.ROWS - 1;
        }
    }*/

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }

    @Override
    public boolean isPlayerControlled() {
        return true;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public void receiveDamage(int damage) {
        health -= damage;
    }

    @Override
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public String toString() {
        return String.format("""
            Player: {
                health: %d/%d
            }
            """, health, maxHealth);
    }
}
