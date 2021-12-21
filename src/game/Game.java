package game;

import game.boids.Flock;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Game itself.
 *
 * @author tobymoszer
 */
public class Game implements ProjectileListener, GameListener {
  
  /**
   * The player of the game
   */
  private Player player;
  
  /**
   * The flock in the game
   */
  private Flock flock;
  
  /**
   * All projectiles fired by the player
   */
  private CopyOnWriteArrayList<Projectile> projectiles;
  
  /**
   * All projectiles fired by the flock
   */
  private CopyOnWriteArrayList<Projectile> flockProjectiles;
  
  /**
   * If the game is over
   */
  private boolean gameOver;
  
  /**
   * The size of the flock
   */
  private final int FLOCK_SIZE = 80;
  
  /**
   * Construct a new game.
   *
   * @param size The size of the game boundaries. This is usually the size of the frame.
   */
  public Game(Vector2 size) {
    player = new Player();
    flock = new Flock(player, FLOCK_SIZE, size, this, this);
    projectiles = new CopyOnWriteArrayList<>();
    flockProjectiles = new CopyOnWriteArrayList<>();
    gameOver = false;
  }
  
  /**
   * Update the player, flock, and all projectiles.
   * Check for all projectile collisions.
   * Check if the game state has changed.
   *
   * @param time The amount of time since the last update in seconds
   */
  public void update(float time) {
    player.update(time);
    flock.update(time);
    
    //check player projectiles
    ArrayList<Projectile> removeProjectiles = new ArrayList<>();
    
    for (Projectile projectile : projectiles) {
      projectile.update(time);
      if (flock.checkProjectile(projectile)) {
        removeProjectiles.add(projectile);
      }
    }
    
    //check flock projectiles
    ArrayList<Projectile> removeFlockProjectiles = new ArrayList<>();
  
    for (Projectile projectile : flockProjectiles) {
      projectile.update(time);
      if (player.checkProjectile(projectile)) {
        removeFlockProjectiles.add(projectile);
        gameOver = true;
      }
    }
    
    //remove dead player projectiles
    for (Projectile projectile : removeProjectiles) {
      projectiles.remove(projectile);
    }
    
    //remove dead flock projectiles
    for (Projectile projectile : removeFlockProjectiles) {
      flockProjectiles.remove(projectile);
    }
    
    //check if the game is won
    if (flock.getSize() == 0) {
      gameOver = true;
    }
    
    //check if the game is over
    if (gameOver) {
      reset();
      gameOver = false;
    }
  }
  
  /**
   * Set the direction that the player is traveling in.
   *
   * @param direction The direction for the player to travel in
   */
  public void setPlayerDirection(Vector2 direction) {
    player.setPlayerDirection(direction);
  }
  
  /**
   * Make the player shoot a projectile.
   *
   * @param point The point at which to aim the projectile at
   */
  public void playerShoot(Point point) {
    projectiles.add(player.shoot(
        new Vector2(
        point.x - player.getPosition().x,
        point.y - player.getPosition().y
        ).normalized()
    ));
  }
  
  /**
   * Reset the game, flock, player, and all projectiles.
   */
  public void reset() {
    flock.reset();
    player.reset();
    projectiles.clear();
    flockProjectiles.clear();
    gameOver = false;
  }
  
  /**
   * Paint the game on the screen.
   * This paints the flock, player, and all projectiles.
   *
   * @param g The Graphics to paint to
   */
  public void paint(Graphics g) {
    flock.paint(g);
    
    for (Projectile projectile : projectiles) {
      projectile.paint(g);
    }
    
    for (Projectile projectile : flockProjectiles) {
      projectile.paint(g, Color.ORANGE);
    }
    
    player.paint(g);
  }
  
  /**
   * Add a new flock projectile.
   *
   * @param projectile The flock projectile to be added
   */
  @Override
  public void addProjectile(Projectile projectile) {
    flockProjectiles.add(projectile);
  }
  
  /**
   * Set the game to be over.
   */
  @Override
  public void isOver() {
    gameOver = true;
  }
}
