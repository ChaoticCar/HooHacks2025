//package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Screen extends JPanel implements ActionListener, KeyListener, MouseListener {

    // controls the delay between each tick in ms
    private final int DELAY = 25;
    // controls the size of the board
    public static final int TILE_SIZE = 120;
    private final int width = 1920;
    private final int height = 1080;
    private int pHeight = 338;
    private int pWidth = 338;
    private final int horizon = height / 2;

    private int pOffset = horizon - pHeight + 64;


    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    private Game game;
    private Player player;
    private String playerScenario = "";
    private String monsterScenario = "";
    private boolean movingLeft = false;

    public Screen(Game game, LLMInterface llmInterface) {
        this.game = game;
        this.player = game.getPlayer(); // Assuming Game class has a getPlayer() method

        // set the game board size
        setPreferredSize(new Dimension(width, height));
        // set the game board background color
        setBackground(new Color(232, 232, 232));

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, horizon, width, 3); // Draws a black line

        for (Sprite sprite : game.getSprites()) {
            int xVal = sprite.getX();
            int yVal = sprite.getY();
            if (sprite.isPlayer() && movingLeft) {
                drawMirroredSprite(g, sprite, xVal, pOffset - yVal);
            } else {
                sprite.draw(g, null, xVal, pOffset - yVal);
            }
        }

        // Draw the player scenario text box if there is text
        if (!playerScenario.isEmpty()) {
            g.setColor(Color.WHITE);
            g.fillRect(10, 10, width / 2 - 20, 100);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, width / 2 - 20, 100);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString(playerScenario, 20, 50);
        }

        // Draw the monster scenario text box if there is text
        if (!monsterScenario.isEmpty()) {
            g.setColor(Color.WHITE);
            g.fillRect(width / 2 + 10, 10, width / 2 - 20, 100);
            g.setColor(Color.BLACK);
            g.drawRect(width / 2 + 10, 10, width / 2 - 20, 100);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString(monsterScenario, width / 2 + 20, 50);
        }
    }

    private void drawMirroredSprite(Graphics g, Sprite sprite, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x + pWidth, y);
        g2d.scale(-1, 1); // Flip horizontally
        sprite.draw(g2d, null, 0, 0); // Draw the sprite at the new origin
        g2d.setTransform(originalTransform); // Restore original transform
    }

    public void updateScenario(String scenario, boolean isPlayerTurn) {
        if (isPlayerTurn) {
            playerScenario = scenario;
        } else {
            monsterScenario = scenario;
        }
        repaint();
    }

    private void movePlayer(int dx) {
        player.setX(player.getX() + dx);
        movingLeft = dx < 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        //player.tick();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // depending on which arrow key or 'A'/'D' key was pressed, we're going to move the player by one whole tile for this input
        if (key == KeyEvent.VK_UP) {
            game.handleInput(GameInput.UP);
        }
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            // TODO: move the player moving logic inside the game-state
            movePlayer(TILE_SIZE);
            game.handleInput(GameInput.RIGHT);
        }
        else if (key == KeyEvent.VK_DOWN) {
            game.handleInput(GameInput.DOWN);
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            // TODO: move the player moving logic inside the game-state
            movePlayer(-TILE_SIZE);
            game.handleInput(GameInput.LEFT);
        }
        else if (key == KeyEvent.VK_I) {
            game.handleInput(GameInput.BUTTON_X);
        }
        //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    
}
