package panel;

import game.Game;
import game.vectors.Vector2;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static sun.audio.AudioPlayer.player;

public class MyPanel extends JPanel implements KeyListener, MouseListener {
  
  private boolean w, a, s, d = false;
  private boolean playerShoot = false;
  
  private Game game;
  
  public MyPanel(Game game) {
    this.game = game;
  }
  
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
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(Color.BLACK);
    game.paint(g);
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
  
  }
  
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
