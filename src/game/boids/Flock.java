package game.boids;

import game.GameListener;
import game.GameMovable;
import game.Player;
import game.ProjectileListener;
import game.projectiles.Projectile;
import game.vectors.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An entire flock.
 *
 * @author tobymoszer
 */
public class Flock implements GameMovable {
  
  /**
   * All members of the flock
   */
  private CopyOnWriteArrayList<FlockMember> members;
  
  /**
   * The AttackType behavior of the flock
   */
  private AttackType attackType;
  
  /**
   * The number of members the flock starts with
   */
  private int originalSize;
  
  /**
   * The position that the flock starts around
   */
  private Vector2 origin;
  
  /**
   * The size of the game boundaries
   */
  private Vector2 gameSize;
  
  /**
   * The distance to apply the separation force
   */
  private final float SEPARATION_DISTANCE = 50;
  
  /**
   * The distance to apply the alignment force
   */
  private final float ALIGNMENT_DISTANCE = 100;
  
  /**
   * The distance to apply the cohesion force
   */
  private final float COHESION_DISTANCE = 150;
  
  /**
   * The player in the game
   */
  private Player player;
  
  /**
   * The chance for the flock to randomly change it's AttackType behavior
   */
  private final float CHANGE_BEHAVIOR_CHANCE = .4f;
  
  /**
   * The ProjectileListener listening for new projectiles to be fired by the flock
   */
  private ProjectileListener projectileListener;
  
  /**
   * The GameListener listening for the game state to be changed
   */
  private GameListener gameListener;
  
  /**
   * Construct a new Flock.
   *
   * @param player The player in the game
   * @param size The initial size of the flock
   * @param gameSize The size of the game boundaries
   * @param projectileListener The projectile listener that will listen for new projectiles to be fired
   * @param gameListener The game listener that will listen for changes in game states
   */
  public Flock(
      Player player,
      int size,
      Vector2 gameSize,
      ProjectileListener projectileListener,
      GameListener gameListener
  ) {
    this(player, size, gameSize, new Vector2(500, 500), projectileListener, gameListener);
  }
  
  /**
   * Construct a new Flock.
   *
   * @param player The player in the game
   * @param size The initial size of the flock
   * @param gameSize The size of the game boundaries
   * @param center The position the flock starts around
   * @param projectileListener The projectile listener that will listen for new projectiles to be fired
   * @param gameListener The game listener that will listen for changes in game states
   */
  public Flock(
      Player player,
      int size,
      Vector2 gameSize,
      Vector2 center,
      ProjectileListener projectileListener,
      GameListener gameListener
  ) {
    members = new CopyOnWriteArrayList<>();
    this.player = player;
    this.gameSize = gameSize;
    this.projectileListener = projectileListener;
    this.gameListener = gameListener;
    originalSize = size;
    origin = center;
    setupFlock(center, size);
  }
  
  /**
   * Create all the new flock members.
   *
   * @param center The position to create the members around
   * @param size The number of members to create
   */
  private void setupFlock(Vector2 center, int size) {
    for (int i = 0; i <size; i++) {
      members.add(new FlockMember(center.copy(), gameSize, false));
    }
    randomBehavior();
  }
  
  /**
   * Update the flock.
   * This also updates all flock members.
   * Update the attack type behavior if necessary.
   *
   * @param time The amount of time since the last update in seconds
   */
  @Override
  public void update(float time) {
    
    calculateForces();
    
    if (Math.random()/time < CHANGE_BEHAVIOR_CHANCE) {
      randomBehavior();
    }
  
    if (Math.random()/time < attackType.getProjectileChance()) {
      fireProjectile();
    }
    
    //add force based on attack type
    attackType.addForces(members, player);
    
    //update all members
    for (FlockMember member : members) {
      member.update(time);
      if (member.hits(player)) {
        gameListener.isOver();
      }
    }
  }
  
  /**
   * Set the attack type behavior to a random attack type.
   */
  private void randomBehavior() {
    attackType = AttackType.randomType();
    
    for (FlockMember member : members) {
      member.setSpeed(attackType.getSpeed());
    }
  }
  
  /**
   * Fire a projectile from a random flock member.
   */
  private void fireProjectile() {
    Projectile projectile =
        members.get((int)(Math.random() * members.size())).fireProjectile(player);
    projectileListener.addProjectile(projectile);
  }
  
  /**
   * Check if the given projectile intersects any flock members.
   *
   * @param projectile The projectile to check against all flock members
   * @return If the given projectile intersects any flock member
   */
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
  
  /**
   * Reset the flock and all it's members.
   */
  @Override
  public void reset() {
    members.clear();
    setupFlock(origin, originalSize);
  }
  
  /**
   * Calculate the main three boids forces.
   */
  private void calculateForces() {
    calculateSeparation();
    calculateAlignment();
    calculateCohesion();
  }
  
  /**
   * Calculate the separation force on all flock members.
   */
  private void calculateSeparation() {
    for (FlockMember member : members) {
      member.calculateSeparation(members, SEPARATION_DISTANCE);
    }
  }
  
  /**
   * Calculate the alignment force on all flock members.
   */
  private void calculateAlignment() {
    for (FlockMember member : members) {
      member.calculateAlignment(members, ALIGNMENT_DISTANCE);
    }
  }
  
  /**
   * Calculate the cohesion force on all flock members.
   */
  private void calculateCohesion() {
    for (FlockMember member : members) {
      member.calculateCohesion(members, COHESION_DISTANCE);
    }
  }
  
  /**
   * Get the current number of flock members.
   * @return The current number of flock members
   */
  public int getSize() {
    return members.size();
  }
  
  /**
   * Paint the flock on the screen.
   *
   * @param g The Graphics to paint to
   */
  public void paint(Graphics g) {
    for (FlockMember member : members) {
      g.setColor(attackType.getColor());
      member.paint(g);
    }
  }
  
  /**
   * Attack type behavior enum.
   */
  private enum AttackType {
    
    /**
     * Default blue behavior.
     * No forces due to the player.
     */
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
      float getSpeed() {
        return 170;
      }
  
      @Override
      Color getColor() {
        return Color.BLUE;
      }
    },
    
    /**
     * Swarm behavior.
     * All members are attracted to the player, no projectiles can be fired.
     */
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
      float getSpeed() {
        return 250;
      }
  
      @Override
      Color getColor() {
        return Color.RED;
      }
    },
    
    /**
     * Avoid behavior.
     * All members avoid the player within a certain range, increased number of projectiles.
     */
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
      float getSpeed() {
        return 170;
      }
  
      @Override
      Color getColor() {
        return Color.YELLOW;
      }
    },
    ;
    
    /**
     * Add forces to the flock members based on the player.
     *
     * @param members All flock members
     * @param player The player
     */
    abstract void addForces(CopyOnWriteArrayList<FlockMember> members, Player player);
    
    /**
     * Get the chance for a projectile to be fired.
     *
     * @return The chance for a projectile to be fired
     */
    abstract float getProjectileChance();
    
    /**
     * Get the speed of the flock members.
     *
     * @return The speed of the flock members
     */
    abstract float getSpeed();
    
    /**
     * Get the color of the flock members.
     *
     * @return The color of the flock members
     */
    abstract Color getColor();
    
    /**
     * Get a random attack type behavior.
     *
     * @return A random attack type behavior
     */
    static AttackType randomType() {
      return values()[(int)(Math.random() * values().length)];
    }
  }
  
}
