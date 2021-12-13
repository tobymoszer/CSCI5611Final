package panel;

import game.Game;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyPanel extends JPanel implements KeyListener, MouseListener {
  
  private Game game;
  
  public MyPanel(Game game) {
    this.game = game;
  }
  
  public void update(float time) {
    game.update(time);
    
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
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
  
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
  
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
}
