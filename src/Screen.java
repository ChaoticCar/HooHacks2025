//package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
    private final int horizon = height/2;


    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    
    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    Game game;
    //Player player;

    public Screen(Game game){

        this.game = game;

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
            sprite.draw(g, null, 0, horizon - pHeight + 64);
        }
    }

    public int getHorizon() {
        return horizon;
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
        //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();

        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == KeyEvent.VK_UP) {
            game.handleInput(GameInput.UP);
        }
        if (key == KeyEvent.VK_RIGHT) {
            game.handleInput(GameInput.RIGHT);
        }
        if (key == KeyEvent.VK_DOWN) {
            game.handleInput(GameInput.DOWN);
        }
        if (key == KeyEvent.VK_LEFT) {
            game.handleInput(GameInput.LEFT);
        }
        //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    
}
