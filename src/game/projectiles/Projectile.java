package game.projectiles;

import game.GameDrawable;
import game.GameMovable;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;

public class Projectile implements GameMovable, GameDrawable {
  
  private Vector2 position;
  private Vector2 direction;
  
  private final int SPEED = 500;
  public final int SIZE = 10;
  
  private Color color = Color.WHITE;
  
  public Projectile(Vector2 position, Vector2 direction) {
    this.position = position;
    this.direction = direction;
  }
  
  @Override
  public void update(float time) {
    position.add(direction.times(time).times(SPEED));
  }
  
  @Override
  public void reset() {
  
  }
  
  public Vector2 getPosition() {
    return position.copy();
  }
  
  @Override
  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
  }
}
