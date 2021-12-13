import game.Game;
import game.vectors.Vector2;
import panel.MyPanel;

import javax.swing.JFrame;

public class Main {
  
  private static final int SIZE_X = 1920;
  private static final int SIZE_Y = 1080;
  
  public static void main(String[] args) {
  
    Game game = new Game(new Vector2(SIZE_X, SIZE_Y));
    
    JFrame frame = new JFrame("Flock Game");
    frame.setSize(SIZE_X, SIZE_Y);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
    MyPanel panel = new MyPanel(game);
    frame.add(panel);
    frame.addKeyListener(panel);
    frame.addMouseListener(panel);
    
    panel.setVisible(true);
    frame.setVisible(true);
    
    frame.repaint();
    panel.repaint();
    
    long time = System.currentTimeMillis();
    
    //main game loop
    while (true) {
      float frameTime = (System.nanoTime() - time)/1000000000f;
      time = System.nanoTime();
      panel.update(frameTime);
    }
    
  }
  
}
