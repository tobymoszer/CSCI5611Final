package game;

import game.boids.Flock;
import game.vectors.Vector2;

import java.awt.Graphics;

public class Game {
  
  private Player player;
  
  private Flock flock;
  
  private final int FLOCK_SIZE = 50;
  
  public Game(Vector2 size) {
    player = new Player();
    flock = new Flock(player, FLOCK_SIZE, size);
  }
  
  public void update(float time) {
    player.update(time);
    flock.update(time);
  }
  
  public void paint(Graphics g) {
    flock.paint(g);
  }
  
}
