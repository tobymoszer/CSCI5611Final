package game;

/**
 * Any GameDrawable that can be moved.
 *
 * @author tobymoszer
 */
public interface GameMovable extends GameDrawable {
  
  void update(float time);
  
  void reset();
  
}
