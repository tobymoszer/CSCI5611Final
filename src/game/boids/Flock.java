package game.boids;

import game.GameMovable;
import game.Player;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Flock implements GameMovable {
  
  private CopyOnWriteArrayList<FlockMember> members;
  private AttackType attackType;
  
  private int originalSize;
  private Vector2 origin;
  
  private Vector2 gameSize;
  
  private final float SEPARATION_DISTANCE = 50;
  private final float ALIGNMENT_DISTANCE = 100;
  private final float COHESION_DISTANCE = 150;
  
  private Player player;
  
  private final float CHANGE_BEHAVIOR_CHANCE = .4f;
  
  private Color color = Color.RED;
  
  public Flock(Player player, int size, Vector2 gameSize) {
    this(player, size, gameSize, new Vector2(500, 500));
  }
  
  public Flock(Player player, int size, Vector2 gameSize, Vector2 center) {
    members = new CopyOnWriteArrayList<>();
    this.player = player;
    this.gameSize = gameSize;
    originalSize = size;
    origin = center;
    setupFlock(center, size);
  }
  
  private void setupFlock(Vector2 center, int size) {
    for (int i = 0; i <size; i++) {
      members.add(new FlockMember(center.copy(), gameSize, false));
    }
    attackType = AttackType.DEFAULT;
  }
  
  @Override
  public void update(float time) {
    
    calculateForces();
    
    if (Math.random()/time < CHANGE_BEHAVIOR_CHANCE) {
      randomBehavior();
    }
  
    if (Math.random()/time < attackType.getProjectileChance()) {
      fireProjectile();
    }
    
    attackType.addForces(members, player);
    
    for (FlockMember member : members) {
      member.update(time);
      if (member.hits(player)) {
        reset();
      }
    }
  }
  
  private void randomBehavior() {
    attackType = AttackType.randomType();
  }
  
  private void fireProjectile() {
    members.get((int)(Math.random() * members.size())).fireProjectile(player);
  }
  
  public boolean checkProjectile(Projectile projectile) {
    ArrayList<FlockMember> removeMembers = new ArrayList<>();
    boolean hit = false;
    
    for (FlockMember member : members) {
      if (member.isHit(projectile)) {
        removeMembers.add(member);
        hit = true;
      }
    }
    
    for (FlockMember member : removeMembers) {
      members.remove(member);
    }
    
    return hit;
  }
  
  @Override
  public void reset() {
    members.clear();
    setupFlock(origin, originalSize);
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
      g.setColor(attackType.getColor());
      member.paint(g);
    }
  }
  
  private enum AttackType {
    DEFAULT {
      @Override
      void addForces(CopyOnWriteArrayList<FlockMember> members, Player player) {
        //nothing added for default
      }
  
      @Override
      float getProjectileChance() {
        return 2;
      }
  
      @Override
      Color getColor() {
        return Color.BLUE;
      }
    },
    SWARM {
      @Override
      void addForces(CopyOnWriteArrayList<FlockMember> members, Player player) {
        for (FlockMember member : members) {
          member.addForceToPlayer(player);
        }
      }
  
      @Override
      float getProjectileChance() {
        return 0;
      }
  
      @Override
      Color getColor() {
        return Color.RED;
      }
    },
    AVOID {
      @Override
      void addForces(CopyOnWriteArrayList<FlockMember> members, Player player) {
        for (FlockMember member : members) {
          member.addForceAwayFromPlayer(player);
        }
      }
  
      @Override
      float getProjectileChance() {
        return 5;
      }
  
      @Override
      Color getColor() {
        return Color.YELLOW;
      }
    },
    ;
    
    abstract void addForces(CopyOnWriteArrayList<FlockMember> members, Player player);
    
    abstract float getProjectileChance();
    
    abstract Color getColor();
    
    static AttackType randomType() {
      return values()[(int)(Math.random() * values().length)];
    }
  }
  
}
