package game;

import game.boids.Flock;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements ProjectileListener, GameListener {
  
  private Player player;
  
  private Flock flock;
  
  private CopyOnWriteArrayList<Projectile> projectiles;
  private CopyOnWriteArrayList<Projectile> flockProjectiles;
  
  private boolean gameOver;
  
  private final int FLOCK_SIZE = 80;
  
  public Game(Vector2 size) {
    player = new Player();
    flock = new Flock(player, FLOCK_SIZE, size, this, this);
    projectiles = new CopyOnWriteArrayList<>();
    flockProjectiles = new CopyOnWriteArrayList<>();
    gameOver = false;
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
    
    ArrayList<Projectile> removeFlockProjectiles = new ArrayList<>();
  
    for (Projectile projectile : flockProjectiles) {
      projectile.update(time);
      if (player.checkProjectile(projectile)) {
        removeFlockProjectiles.add(projectile);
        gameOver = true;
      }
    }
    
    for (Projectile projectile : removeProjectiles) {
      projectiles.remove(projectile);
    }
    
    for (Projectile projectile : removeFlockProjectiles) {
      flockProjectiles.remove(projectile);
    }
    
    if (flock.getSize() == 0) {
      gameOver = true;
    }
    
    if (gameOver) {
      reset();
      gameOver = false;
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
    flockProjectiles.clear();
  }
  
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
  
  @Override
  public void addProjectile(Projectile projectile) {
    flockProjectiles.add(projectile);
  }
  
  @Override
  public void isOver() {
    gameOver = true;
  }
}
