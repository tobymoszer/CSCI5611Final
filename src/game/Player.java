package game;

import game.projectiles.Projectile;
import game.vectors.Vector2;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player implements GameMovable{
  
  private Vector2 position;
  private Vector2 direction;
  
  private final float SPEED = 300;
  public final int SIZE = 40;
  
  private Color color = Color.WHITE;
  
  private BufferedImage image;
  
  public Player() {
    position = new Vector2(800, 800);
    direction = new Vector2(0, 0);
  
    try {
      image = ImageIO.read(new File("sprites/white_eye.png"));
    } catch (IOException e) {
      System.err.println("Could not load eye image");
      e.printStackTrace();
    }
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
    //g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
    
    g.drawImage(image, (int) position.x, (int) position.y, SIZE, SIZE, null);
    
  }
}
