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

    // controls the final int DELAY = 25;
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
    Timer timer;

    private Game game;
    private Player player;
    private String playerScenario = "";
    private String monsterScenario = "";
    private boolean movingLeft = false;
    private String[] playerOptions = new String[3];

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
            if (sprite == player && movingLeft) {
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

        // Draw player options if available
        if (playerOptions[0] != null) {
            g.setColor(Color.WHITE);
            g.fillRect(10, 120, width - 20, 100);
            g.setColor(Color.BLACK);
            g.drawRect(10, 120, width - 20, 100);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            for (int i = 0; i < playerOptions.length; i++) {
                g.drawString((i + 1) + ". " + playerOptions[i], 20, 150 + (i * 20));
            }
        }
    }

    private void drawMirroredSprite(Graphics g, Sprite sprite, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x + sprite.getWidth(), y);
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

    public void updateOptions(String[] options) {
        playerOptions = options;
        repaint();
    }

    private void movePlayer(int dx) {
        player.setX(player.getX() + dx);
        movingLeft = dx < 0;
    }
}