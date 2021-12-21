package game;

import game.projectiles.Projectile;
import game.vectors.Vector2;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Player in the Game.
 *
 * @author tobymoszer
 */
public class Player implements GameMovable{
  
  /**
   * The position of the player
   */
  private Vector2 position;
  
  /**
   * The direction the player is facing
   */
  private Vector2 direction;
  
  /**
   * The speed that the player moves
   */
  private final float SPEED = 300;
  
  /**
   * The size of the player on the screen
   */
  public final int SIZE = 40;
  
  /**
   * The color of the player when the sprite is not used
   */
  private Color color = Color.WHITE;
  
  /**
   * The player sprite
   */
  private BufferedImage image;
  
  /**
   * Constructs a new Player.
   */
  public Player() {
    position = new Vector2(800, 800);
    direction = new Vector2(0, 0);
  
    //load the sprite image
    try {
      image = ImageIO.read(new File("sprites/white_eye.png"));
    } catch (IOException e) {
      System.err.println("Could not load eye image");
      e.printStackTrace();
    }
  }
  
  /**
   * Update the player's position.
   *
   * @param time The amount of time since the last update in seconds
   */
  @Override
  public void update(float time) {
    position.add(direction.times(time).times(SPEED));
  }
  
  /**
   * Set the direction that the player is traveling in.
   *
   * @param direction The direction for the player to travel in
   */
  public void setPlayerDirection(Vector2 direction) {
    this.direction = direction;
  }
  
  /**
   * Fire a projectile from the player.
   *
   * @param aim The direction the projectile will be shot
   * @return The shot projectile
   */
  public Projectile shoot(Vector2 aim) {
    Projectile projectile = new Projectile(position.copy(), aim);
    return projectile;
  }
  
  /**
   * Get the position of the player.
   *
   * @return The player's position
   */
  public Vector2 getPosition() {
    return position.copy();
  }
  
  /**
   * Check if the given projectile is intersecting the player.
   *
   * @param projectile The projectile to check
   * @return If the projectile intersects the player
   */
  public boolean checkProjectile(Projectile projectile) {
    return position.minus(projectile.getPosition()).length() <
        SIZE + projectile.SIZE;
  }
  
  /**
   * Reset the player's position.
   */
  @Override
  public void reset() {
    position.x = 800;
    position.y = 800;
  }
  
  /**
   * Paint the player on the screen.
   *
   * @param g The Graphics to paint to
   */
  @Override
  public void paint(Graphics g) {
    g.setColor(color);
    //g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
    
    g.drawImage(image, (int) position.x, (int) position.y, SIZE, SIZE, null);
    
  }
}
