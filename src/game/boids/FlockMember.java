package game.boids;

import game.GameMovable;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class FlockMember implements GameMovable {
  
  private Vector2 separation, alignment, cohesion;
  private Vector2 acceleration;
  private Vector2 position;
  private Vector2 velocity;
  
  private final float SEPARATION_WEIGHT = 1f;
  private final float ALIGNMENT_WEIGHT = .2f;
  private final float COHESION_WEIGHT = 1f;
  
  private final float SPEED = 170f;
  private final float MAX_FORCE = 1;
  
  private Color color = Color.RED;
  
  private int size = 20;
  
  public FlockMember(Vector2 position, boolean exactPosition) {
    if (exactPosition) {
      this.position = position;
    } else {
      this.position = new Vector2(
          position.x + (int) (Math.random() * 100),
          position.y + (int) (Math.random() * 100)
      );
    }
    separation = new Vector2(0, 0);
    alignment = new Vector2(0, 0);
    cohesion = new Vector2(0, 0);
    acceleration = new Vector2(0, 0);
    velocity = new Vector2(0, 0);
  }
  
  public void calculateSeparation(ArrayList<FlockMember> members, float distance) {
    for (FlockMember other : members) {
      if (!other.equals(this)) {
        if (position.minus(other.position).length() < distance) {
          Vector2 currentSeparation = position.minus(other.position);
          currentSeparation.setToLength(
              200f/currentSeparation.length()
          );
          separation.add(currentSeparation);
        }
      }
    }
  }
  
  public void calculateAlignment(ArrayList<FlockMember> members, float distance) {
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
    }
  }
  
  public void calculateCohesion(ArrayList<FlockMember> members, float distance) {
    
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
      cohesion = averagePosition.minus(position).normalized();
    }
    
  }
  
  @Override
  public void update(float time) {
  
    //System.out.println(separation + " " + alignment + " " + cohesion);
    
    Vector2 force = Vector2.zero();
  
    force.add(separation.times(SEPARATION_WEIGHT));
    force.add(alignment.times(ALIGNMENT_WEIGHT));
    force.add(cohesion.times(COHESION_WEIGHT));
    //force.add(Vector2.randomVector(1));
    
    force.clampToLength(MAX_FORCE);
    
    acceleration.add(force);
  
    position.add(velocity.times(time));
    velocity.add(acceleration.times(time));
    velocity.clampToLength(SPEED);
    
    
    separation.reset();
    alignment.reset();
    cohesion.reset();
  }
  
  @Override
  public void reset() {
    
    separation.reset();
    alignment.reset();
    cohesion.reset();
    velocity.reset();
    acceleration.reset();
  
    position = new Vector2(
        500 + (int) (Math.random() * 100),
        500 + (int) (Math.random() * 100)
    );
  }
  
  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, size, size);
  }
  
}
