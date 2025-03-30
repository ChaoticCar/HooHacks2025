import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics; // Added import for FontMetrics
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
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
    private double isFlipped;
    private int pOffset = horizon - pHeight + 64;

    private final int textLineOffset = 15;

    private JButton button1;
    private JButton button2;
    private JButton button3;


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

    public Screen(Game game, LLMInterface llmInterface, JButton button1, JButton button2, JButton button3) {

        //this.addEventFilter(MouseEvent.ANY, (e) -> this.requestFocus());

        this.game = game;
        this.player = game.getPlayer(); // Assuming Game class has a getPlayer() method

        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;

        setLayout(null);
        button1.setFocusable(false);
        button2.setFocusable(false);
        button3.setFocusable(false);

        add(button1);
        add(button2);
        add(button3);

        // Set button bounds (x, y, width, height)
        button1.setBounds(50, height - 350, 150, 40);
        button2.setBounds(250, height - 350, 150, 40);
        button3.setBounds(450, height - 350, 150, 40);

        // again, sorry for this
        SituationGen.initialize();

        // Add action listeners for button presses
        button1.addActionListener(this::handleButton1);
        button2.addActionListener(this::handleButton2);
        button3.addActionListener(this::handleButton3);

        button1.setText("Attack");

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
                isFlipped = .35;
                drawMirroredSprite(g, sprite, xVal, pOffset - yVal);
            } else {
                isFlipped = 1;
                sprite.draw(g, null, xVal, pOffset - yVal);
            }
        }

        // Draw the player scenario text box if there is text
        if (!playerScenario.isEmpty()) {
            g.setColor(Color.WHITE);
            g.fillRect(10, 10, 3 * width / 5, 150);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 3 * width / 5, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            drawString(g, playerScenario, 20, 30, width / 2 - 40, 80); // Adjusted for text wrapping

        }

        // Draw the monster scenario text box if there is text
        if (!monsterScenario.isEmpty()) {
            g.setColor(Color.WHITE);
            g.fillRect(10, 10, 3 * width / 5 - 20, 150);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 3 * width / 5 - 20, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            drawString(g, monsterScenario, width / 2 + 20, 30, width / 2 - 40, 80); // Adjusted for text wrapping
        }
        drawHealthBar(g);

        ArrayList<String> actions = SituationGen.getPlayerActions();
        button1.setText(actions.get(0));
        button2.setText(actions.get(1));
        button3.setText(actions.get(2));
    }

    private void drawMirroredSprite(Graphics g, Sprite sprite, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x + pHeight, y);
        g2d.scale(-1, 1); // Flip horizontally
        sprite.draw(g2d, null, 0, 0); // Draw the sprite at the new origin
        g2d.setTransform(originalTransform); // Restore original transform
    }

    private void drawHealthBar(Graphics g2d) {
        int barWidth = pWidth / 2;
        int barHeight = 16;
        int x = (int)(player.getX() + (pWidth/2) - isFlipped * 156) + 16;
        int y = height - 32 - 800 + 16;
        int max = player.getMaxHealth();
        int health = player.getHealth();

        // Background of HP bar
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);

        // Current HP
        g2d.setColor(Color.RED);
        g2d.fillRect(x, y,(int)((double)(barWidth * (health / max))), barHeight);

        // Border of the HP bar
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, barWidth, barHeight);

        // HP number
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.BLACK);
        g2d.drawString("HP: " + player.getHealth(), x + barWidth + 10, y + barHeight - 5);
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

    private void startCombat() {
        System.out.println("starting combat");


        // Logic to start combat
        String scenario = SituationGen.run(0, player, game.getMonster(), "Attack"); // Start with player's turn and no current monster
        String[] words = scenario.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            builder.append(words[i]).append(" ");
            if (i % 30 == 25) {
                builder.append("\n");
            }
        }
        scenario = builder.toString();

        updateScenario(scenario, true); // Update the scenario for the player
    }

    private void continueCombat(String playerAction) {
        // Logic to start next round of combat
        String scenario = SituationGen.run(SituationGen.getTurnCount() + 1, player, game.getMonster(), playerAction); // Start with player's turn and no current monster
        String[] words = scenario.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            builder.append(words[i]).append(" ");
            if (i % 30 == 25) {
                builder.append("\n");
            }
        }
        scenario = builder.toString();

        updateScenario(scenario, true); // Update the scenario for the player
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        // player.tick();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    public void handleButton1(ActionEvent e) {
        if (SituationGen.getTurnCount() != 0) {
            continueCombat(button1.getText());
        } else {
            startCombat();
        }
    }

    public void handleButton2(ActionEvent e) {
        if (SituationGen.getTurnCount() != 0) {
            continueCombat(button2.getText());
        } else {
            startCombat();
        }
    }

    public void handleButton3(ActionEvent e) {
        if (SituationGen.getTurnCount() != 0) {
            continueCombat(button3.getText());
        } else {
            startCombat();
        }
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
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movePlayer(TILE_SIZE);
            game.handleInput(GameInput.RIGHT);
        }
        if (key == KeyEvent.VK_DOWN) {
            game.handleInput(GameInput.DOWN);
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movePlayer(-TILE_SIZE);
            game.handleInput(GameInput.LEFT);
        }
        if (key == KeyEvent.VK_E) {
            startCombat();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    public void drawString(Graphics g, String text, int x, int y, int width, int height) {
        FontMetrics metrics = g.getFontMetrics();
        int lineHeight = metrics.getHeight();
        int currentX = x;
        int currentY = y + metrics.getAscent() - 12;

        String[] words = text.split(" ");
        for (String word : words) {
            int wordWidth = metrics.stringWidth(word + " ");
            if (currentX + wordWidth > x + width + 200) {
                currentY += lineHeight;
                currentX = x;
            }
            if (currentY > y + height + 62) {
                break; // Stop drawing if text exceeds box height
            }
            g.drawString(word, currentX, currentY);
            currentX += wordWidth;
        }
    }
}