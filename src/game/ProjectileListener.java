package game;

import game.projectiles.Projectile;

/**
 * An object that listens for new projectiles to exist.
 *
 * @author tobymoszer
 */
public interface ProjectileListener {
  
  void addProjectile(Projectile projectile);
  
}
