package panel;

import game.Game;
import game.vectors.Vector2;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A JPanel that keeps track of keyboard and mouse inputs,
 * as well as paints the game.
 *
 * @author tobymoszer
 */
public class MyPanel extends JPanel implements KeyListener, MouseListener {
  
  /**
   * Which keys are currently held down
   */
  private boolean w, a, s, d = false;
  
  /**
   * If the player should shoot
   */
  private boolean playerShoot = false;
  
  /**
   * The game
   */
  private Game game;
  
  /**
   * The background image
   */
  private BufferedImage background;
  
  /**
   * Constructs a new panel.
   *
   * @param game The game that the panel will contain
   */
  public MyPanel(Game game) {
    this.game = game;
    
    //load in background image
    try {
      background = ImageIO.read(new File("sprites/background.png"));
    } catch (IOException e) {
      System.err.println("Could not load background image");
      e.printStackTrace();
    }
  }
  
  /**
   * Update the panel and the game, and repaint.
   *
   * @param time The time since the previous update in seconds
   */
  public void update(float time) {
    
    game.update(time);
    
    if (w) {
      if (a) {
        game.setPlayerDirection(new Vector2(-1, -1).normalized());
      } else if (d) {
        game.setPlayerDirection(new Vector2(1, -1).normalized());
      } else if (!s) {
        game.setPlayerDirection(new Vector2(0, -1));
      }
    } else if (a) {
      if (s) {
        game.setPlayerDirection(new Vector2(-1, 1).normalized());
      } else if (!d) {
        game.setPlayerDirection(new Vector2(-1, 0));
      }
    } else if (s) {
      if (d) {
        game.setPlayerDirection(new Vector2(1, 1).normalized());
      } else {
        game.setPlayerDirection(new Vector2(0, 1));
      }
    } else if (d) {
      game.setPlayerDirection(new Vector2(1, 0));
    } else {
      game.setPlayerDirection(new Vector2(0, 0));
    }
    
    if (playerShoot) {
      playerShoot = false;
      game.playerShoot(MouseInfo.getPointerInfo().getLocation());
    }
    
    repaint();
  }
  
  /**
   * Paint the background image and the game.
   *
   * @param g The Graphics to paint to
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, null);
    game.paint(g);
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
  
  }
  
  /**
   * Gets keys pressed and sets movement and reset functionality.
   *
   * @param e The KeyEvent
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyChar() == 'r') {
      game.reset();
    }
    if (e.getKeyChar() == 'w') {
      w = true;
    }
    if (e.getKeyChar() == 'a') {
      a = true;
    }
    if (e.getKeyChar() == 's') {
      s = true;
    }
    if (e.getKeyChar() == 'd') {
      d = true;
    }
  }
  
  /**
   * Gets keys released and sets movement functionality.
   *
   * @param e The KeyEvent
   */
  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyChar() == 'w') {
      w = false;
    }
    if (e.getKeyChar() == 'a') {
      a = false;
    }
    if (e.getKeyChar() == 's') {
      s = false;
    }
    if (e.getKeyChar() == 'd') {
      d = false;
    }
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
  
  }
  
  /**
   * Sets the playerShoot boolean when the mouse is clicked.
   *
   * @param e The MouseEvent
   */
  @Override
  public void mousePressed(MouseEvent e) {
    playerShoot = true;
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
}
