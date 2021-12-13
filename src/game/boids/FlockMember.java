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
  
  private final float speed = 170f;
  
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
    
    acceleration.add(separation);
    acceleration.add(alignment);
    acceleration.add(cohesion);
    acceleration.add(Vector2.randomVector(2000));
  
    position.add(velocity.times(time));
    velocity.add(acceleration.times(time));
    velocity.clampToLength(speed);
    
    
    separation.reset();
    alignment.reset();
    cohesion.reset();
  }
  
  public void paint(Graphics g) {
    g.setColor(color);
    g.fillOval((int) position.x, (int) position.y, size, size);
  }
  
}
