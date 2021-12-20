package game.boids;

import game.GameMovable;
import game.Player;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlockMember implements GameMovable {
  
  private Vector2 separation, alignment, cohesion;
  private Vector2 attackForce;
  private Vector2 acceleration;
  private Vector2 position;
  private Vector2 velocity;
  
  private Vector2 gameSize;
  
  private final float SEPARATION_WEIGHT = 3f;
  private final float ALIGNMENT_WEIGHT = .01f;
  private final float COHESION_WEIGHT = 1f;
  private final float ATTACK_WEIGHT = 1f;
  
  private float speed;
  private final float MAX_FORCE = 85f;
  
  private int size = 20;
  
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
  
  private void scatter() {
    position.x += (int) (Math.random() * 100);
    position.y += (int) (Math.random() * 100);
  }
  
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
  
  public void addForceToPlayer(Player player) {
    attackForce = player.getPosition().minus(position);
  }
  
  public void addForceAwayFromPlayer(Player player) {
    if (position.minus(player.getPosition()).length() < 600) {
      attackForce = position.minus(player.getPosition());
      attackForce.setToLength(
          200/(Math.pow(position.minus(player.getPosition()).length(), 2))
      );
    }
  }
  
  @Override
  public void update(float time) {
  
    //System.out.println(separation + " " + alignment + " " + cohesion);
    
    Vector2 force = Vector2.zero();
  
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
  
    force.add(separation.times(SEPARATION_WEIGHT));
    force.add(alignment.times(ALIGNMENT_WEIGHT));
    force.add(cohesion.times(COHESION_WEIGHT));
    force.add(attackForce.times(ATTACK_WEIGHT));
    force.add(Vector2.randomVector(1));
    
    force.clampToLength(MAX_FORCE);
    
    if (force.exists()) {
      acceleration.add(force);
    }
    
    position.add(velocity.times(time));
    velocity.add(acceleration.times(time));
    velocity.clampToLength(speed);
    
    checkOutOfBounds();
    
    separation.reset();
    alignment.reset();
    cohesion.reset();
    attackForce.reset();
  }
  
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
  
  public boolean hits(Player player) {
    return position.minus(player.getPosition()).length() <
        size + player.SIZE;
  }
  
  public boolean isHit(Projectile projectile) {
    return position.minus(projectile.getPosition()).length() <
        size + projectile.SIZE;
  }
  
  public Projectile fireProjectile(Player player) {
    return new Projectile(
        position.copy(),
        new Vector2(
            player.getPosition().x - position.x,
            player.getPosition().y - position.y
        ).normalized()
    );
  }
  
  public void setSpeed(float speed) {
    this.speed = speed;
  }
  
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
