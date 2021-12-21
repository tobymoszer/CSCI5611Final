package game.boids;

import game.GameMovable;
import game.Player;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A single member of a Flock.
 *
 * @author tobymoszer
 */
public class FlockMember implements GameMovable {
  
  /**
   * The three main boids forces
   */
  private Vector2 separation, alignment, cohesion;
  
  /**
   * The force to attack or avoid the player
   */
  private Vector2 attackForce;
  
  /**
   * The acceleration of the flock member
   */
  private Vector2 acceleration;
  
  /**
   * The position of the flock member
   */
  private Vector2 position;
  
  /**
   * The velocity of the flock member
   */
  private Vector2 velocity;
  
  /**
   * The size of the game boundaries
   */
  private Vector2 gameSize;
  
  /**
   * The weight of the separation force
   */
  private final float SEPARATION_WEIGHT = 3f;
  
  /**
   * The weight of the alignment force
   */
  private final float ALIGNMENT_WEIGHT = .01f;
  
  /**
   * The weight of the cohesion force
   */
  private final float COHESION_WEIGHT = 1f;
  
  /**
   * The weight of the player attack force
   */
  private final float ATTACK_WEIGHT = 1f;
  
  /**
   * The speed of the flock member
   */
  private float speed;
  
  /**
   * The maximum magnitude of a force that can be applied to the flock member
   */
  private final float MAX_FORCE = 85f;
  
  /**
   * The size of the flock member on the screen
   */
  private int size = 20;
  
  /**
   * Construct a FlockMember.
   *
   * @param position The position of the flock member
   * @param gameSize The size of the game boundaries
   * @param exactPosition If the flock member should be placed at that exact position, or just nearby
   */
  public FlockMember(Vector2 position, Vector2 gameSize, boolean exactPosition) {
    this.position = position;
    if (!exactPosition) {
      scatter();
    }
    this.gameSize = gameSize;
    separation = new Vector2(0, 0);
    alignment = new Vector2(0, 0);
    cohesion = new Vector2(0, 0);
    attackForce = new Vector2(0, 0);
    acceleration = new Vector2(0, 0);
    velocity = new Vector2(0, 0);
  }
  
  /**
   * Scatter the flock members to nearby locations
   */
  private void scatter() {
    position.x += (int) (Math.random() * 100);
    position.y += (int) (Math.random() * 100);
  }
  
  /**
   * Calculate the separation force on the flock member.
   *
   * @param members The other flock members
   * @param distance The distance to check for other flock members
   */
  public void calculateSeparation(CopyOnWriteArrayList<FlockMember> members, float distance) {
    for (FlockMember other : members) {
      if (!other.equals(this)) {
        if (position.minus(other.position).length() < distance) {
          Vector2 currentSeparation = position.minus(other.position).normalized();
          currentSeparation.setToLength(
              200f/Math.pow(currentSeparation.length(), 2)
          );
          separation.add(currentSeparation);
        }
      }
    }
  }
  
  /**
   * Calculate the alignment force on the flock member.
   *
   * @param members The other flock members
   * @param distance The distance to check for other flock members
   */
  public void calculateAlignment(CopyOnWriteArrayList<FlockMember> members, float distance) {
    Vector2 averageVelocity = new Vector2(0, 0);
    int neighbors = 0;
    
    for (FlockMember other : members) {
      if (!other.equals(this)) {
        if (position.minus(other.position).length() < distance) {
          averageVelocity.add(other.velocity);
          neighbors++;
        }
      }
    }
    
    if (neighbors != 0) {
      averageVelocity.over(neighbors);
      alignment = averageVelocity.minus(velocity);
    } else {
      alignment = Vector2.zero();
    }
  }
  
  /**
   * Calculate the cohesion force on the flock member.
   *
   * @param members The other flock members
   * @param distance The distance to check for other flock members
   */
  public void calculateCohesion(CopyOnWriteArrayList<FlockMember> members, float distance) {
    
    Vector2 averagePosition = new Vector2(0, 0);
    int neighbors = 0;
    
    for (FlockMember other : members) {
      if (!other.equals(this)) {
        if (position.minus(other.position).length() < distance) {
          averagePosition.add(other.position);
          neighbors ++;
        }
      }
    }
    
    if (neighbors != 0) {
      averagePosition.over(neighbors);
      cohesion = averagePosition.minus(position);
    }
    
  }
  
  /**
   * Add the force toward the player.
   *
   * @param player The player
   */
  public void addForceToPlayer(Player player) {
    attackForce = player.getPosition().minus(position);
  }
  
  /**
   * Add the force away from the player.
   *
   * @param player The player
   */
  public void addForceAwayFromPlayer(Player player) {
    if (position.minus(player.getPosition()).length() < 600) {
      attackForce = position.minus(player.getPosition());
      attackForce.setToLength(
          200/(Math.pow(position.minus(player.getPosition()).length(), 2))
      );
    }
  }
  
  /**
   * Update the flock member.
   * Add all forces and compensate for their weights.
   * Calculate the acceleration, velocity, and position of the flock member.
   *
   * @param time The amount of time since the last update in seconds
   */
  @Override
  public void update(float time) {
    
    Vector2 force = Vector2.zero();
  
    //check if the forces exist
    if (!separation.isZero()) {
      separation.normalize();
    }
    if (!alignment.isZero()) {
      alignment.normalize();
    }
    if (!cohesion.isZero()) {
      cohesion.normalize();
    }
    if (!attackForce.isZero()) {
      attackForce.normalize();
    }
  
    //add all forces with their weights
    force.add(separation.times(SEPARATION_WEIGHT));
    force.add(alignment.times(ALIGNMENT_WEIGHT));
    force.add(cohesion.times(COHESION_WEIGHT));
    force.add(attackForce.times(ATTACK_WEIGHT));
    force.add(Vector2.randomVector(1));
    
    //make sure the max force is not exceeded
    force.clampToLength(MAX_FORCE);
    
    //add the force to the acceleration
    if (force.exists()) {
      acceleration.add(force);
    }
    
    //update position and velocity
    position.add(velocity.times(time));
    velocity.add(acceleration.times(time));
    velocity.clampToLength(speed);
    
    //check if the flock member is out of bounds
    checkOutOfBounds();
    
    //reset forces
    separation.reset();
    alignment.reset();
    cohesion.reset();
    attackForce.reset();
  }
  
  /**
   * Check if the flock member is out of bounds of the game.
   * If it is, set the position to wrap around the screen.
   */
  private void checkOutOfBounds() {
    if (position.x > gameSize.x) {
      position.x -= gameSize.x;
    }
    if (position.x < 0) {
      position.x += gameSize.x;
    }
    if (position.y > gameSize.y) {
      position.y -= gameSize.y;
    }
    if (position.y < 0) {
      position.y += gameSize.y;
    }
  }
  
  /**
   * Check if the flock member intersects the given player.
   *
   * @param player The player to check intersection with
   * @return If the flock member intersects the player
   */
  public boolean hits(Player player) {
    return position.minus(player.getPosition()).length() <
        size + player.SIZE;
  }
  
  /**
   * Check if the flock member is hit by the given projectile.
   *
   * @param projectile The projectile to check intersection with
   * @return If the flock member is hit by the projectile
   */
  public boolean isHit(Projectile projectile) {
    return position.minus(projectile.getPosition()).length() <
        size + projectile.SIZE;
  }
  
  /**
   * Fire a projectile at the given player.
   *
   * @param player The player to aim at
   * @return The fired projectile
   */
  public Projectile fireProjectile(Player player) {
    return new Projectile(
        position.copy(),
        new Vector2(
            player.getPosition().x - position.x,
            player.getPosition().y - position.y
        ).normalized()
    );
  }
  
  /**
   * Set the speed of the flock member.
   *
   * @param speed The speed to set to
   */
  public void setSpeed(float speed) {
    this.speed = speed;
  }
  
  /**
   * Reset the flock member.
   * Resets all forces and position.
   */
  @Override
  public void reset() {
    
    separation.reset();
    alignment.reset();
    cohesion.reset();
    velocity.reset();
    acceleration.reset();
  
    position = new Vector2(500, 500);
    scatter();
  }
  
  /**
   * Paint the flock member on the screen.
   * This also paints a small circle in front of the flock member indicating it's direction.
   *
   * @param g The Graphics to paint to
   */
  public void paint(Graphics g) {
    g.fillOval((int) position.x, (int) position.y, size, size);
    
    Vector2 direction = velocity.normalized();
    direction.setToLength(size * 1.5);
    g.fillOval(
        (int) (position.x + direction.x) + size/2,
        (int) (position.y + direction.y) + size/2,
        size/3,
        size/3
    );
    
  }
  
}
