package game;

import game.boids.Flock;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
  
  private Player player;
  
  private Flock flock;
  
  private CopyOnWriteArrayList<Projectile> projectiles;
  private CopyOnWriteArrayList<Projectile> flockProjectiles;
  
  private final int FLOCK_SIZE = 80;
  
  public Game(Vector2 size) {
    player = new Player();
    flock = new Flock(player, FLOCK_SIZE, size);
    projectiles = new CopyOnWriteArrayList<>();
  }
  
  public void update(float time) {
    player.update(time);
    flock.update(time);
    
    ArrayList<Projectile> removeProjectiles = new ArrayList<>();
    
    for (Projectile projectile : projectiles) {
      projectile.update(time);
      if (flock.checkProjectile(projectile)) {
        removeProjectiles.add(projectile);
      }
    }
    
    for (Projectile projectile : removeProjectiles) {
      projectiles.remove(projectile);
    }
  }
  
  public void setPlayerDirection(Vector2 direction) {
    player.setPlayerDirection(direction);
  }
  
  public void playerShoot(Point point) {
    projectiles.add(player.shoot(
        new Vector2(
        point.x - player.getPosition().x,
        point.y - player.getPosition().y
        ).normalized()
    ));
  }
  
  public void reset() {
    flock.reset();
    player.reset();
    projectiles.clear();
  }
  
  public void paint(Graphics g) {
    flock.paint(g);
    
    
    for (Projectile projectile : projectiles) {
      projectile.paint(g);
    }
    player.paint(g);
  }
  
}
