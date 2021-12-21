package game.projectiles;

import game.GameDrawable;
import game.GameMovable;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A Projectile shot by any Game element.
 *
 * @author tobymoszer
 */
public class Projectile implements GameMovable, GameDrawable {
  
  /**
   * The position of the projectile
   */
  private Vector2 position;
  
  /**
   * The direction the projectile is moving in
   */
  private Vector2 direction;
  
  /**
   * The speed the projectile is traveling at
   */
  private final int SPEED = 500;
  
  /**
   * The size of the projectile on the screen
   */
  public final int SIZE = 10;
  
  /**
   * The color of the projectile
   */
  private Color color = Color.WHITE;
  
  /**
   * Construct a new projectile.
   *
   * @param position The original position
   * @param direction The direction for the projectile to travel in
   */
  public Projectile(Vector2 position, Vector2 direction) {
    this.position = position;
    this.direction = direction;
  }
  
  /**
   * Update the projectile's position.
   *
   * @param time The amount of time since the last update in seconds
   */
  @Override
  public void update(float time) {
    position.add(direction.times(time).times(SPEED));
  }
  
  /**
   * Reset the projectile.
   */
  @Override
  public void reset() {
  
  }
  
  /**
   * Get the position of the projectile.
   *
   * @return The projectile's position
   */
  public Vector2 getPosition() {
    return position.copy();
  }
  
  /**
   * Paint the projectile on the screen.
   *
   * @param g The Graphics to paint to
   */
  @Override
  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
  }
  
  /**
   * Paint the projectile on the screen with the given color.
   *
   * @param g The Graphics to paint to
   * @param color The Color to paint the projectile with
   */
  public void paint(Graphics g, Color color) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
  }
}
