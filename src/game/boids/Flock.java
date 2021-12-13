package game.boids;

import game.GameMovable;
import game.Player;
import game.vectors.Vector2;

import java.awt.Graphics;
import java.util.ArrayList;

public class Flock implements GameMovable {
  
  private ArrayList<FlockMember> members;
  
  private final float SEPARATION_DISTANCE = 80;
  private final float ALIGNMENT_DISTANCE = 100;
  private final float COHESION_DISTANCE = 70;
  
  private Player player;
  
  public Flock(Player player, int size, Vector2 gameSize) {
    members = new ArrayList<>(size);
    this.player = player;
    setupFlock(new Vector2(500, 500), size);
  }
  
  public Flock(Player player, int size, Vector2 gameSize, Vector2 center) {
    members = new ArrayList<>(size);
    this.player = player;
    setupFlock(center, size);
  }
  
  private void setupFlock(Vector2 center, int size) {
    for (int i = 0; i <size; i++) {
      members.add(new FlockMember(center, false));
    }
  }
  
  @Override
  public void update(float time) {
    calculateForces();
    for (FlockMember member : members) {
      member.update(time);
    }
  }
  
  private void calculateForces() {
    calculateSeparation();
    calculateAlignment();
    calculateCohesion();
  }
  
  private void calculateSeparation() {
    for (FlockMember member : members) {
      member.calculateSeparation(members, SEPARATION_DISTANCE);
    }
  }
  
  private void calculateAlignment() {
    for (FlockMember member : members) {
      member.calculateAlignment(members, ALIGNMENT_DISTANCE);
    }
  }
  
  private void calculateCohesion() {
    for (FlockMember member : members) {
      member.calculateCohesion(members, COHESION_DISTANCE);
    }
  }
  
  public void paint(Graphics g) {
    for (FlockMember member : members) {
      member.paint(g);
    }
  }
  
}
