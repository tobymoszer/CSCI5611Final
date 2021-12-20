package game;

import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;

public class Player implements GameMovable{
  
  private Vector2 position;
  private Vector2 direction;
  
  private final float SPEED = 300;
  public final int SIZE = 20;
  
  private Color color = Color.WHITE;
  
  public Player() {
    position = new Vector2(800, 800);
    direction = new Vector2(0, 0);
  }
  
  @Override
  public void update(float time) {
    position.add(direction.times(time).times(SPEED));
  }
  
  public void setPlayerDirection(Vector2 direction) {
    this.direction = direction;
  }
  
  public Projectile shoot(Vector2 aim) {
    Projectile projectile = new Projectile(position.copy(), aim);
    return projectile;
  }
  
  public Vector2 getPosition() {
    return position.copy();
  }
  
  public boolean checkProjectile(Projectile projectile) {
    return position.minus(projectile.getPosition()).length() <
        SIZE + projectile.SIZE;
  }
  
  @Override
  public void reset() {
    position.x = 800;
    position.y = 800;
  }
  
  @Override
  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
  }
}
