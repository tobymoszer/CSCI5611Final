package game.vectors;

/**
 * Vector Library
 * CSCI 5611 Vector 2 Library [Example]
 * Stephen J. Guy <sjguy@umn.edu>
 * Adjusted for this project by Tobias Moszer <mosze006@umn.edu>
 */
public class Vector2 {
  
  public double x, y;
  
  public Vector2(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public String toString() {
    return "(" + x+ "," + y +")";
  }
  
  public void reset() {
    x = 0;
    y = 0;
  }
  
  public double length() {
    return Math.sqrt(x*x+y*y);
  }
  
  public double lengthSqr() {
    return x*x+y*y;
  }
  
  public Vector2 plus(Vector2 rhs) {
    return new Vector2(x+rhs.x, y+rhs.y);
  }
  
  public void add(Vector2 rhs) {
    x += rhs.x;
    y += rhs.y;
  }
  
  public Vector2 minus(Vector2 rhs) {
    return new Vector2(x-rhs.x, y-rhs.y);
  }
  
  public void subtract(Vector2 rhs) {
    x -= rhs.x;
    y -= rhs.y;
  }
  
  public Vector2 times(double rhs) {
    return new Vector2(x*rhs, y*rhs);
  }
  
  public void mul(double rhs) {
    x *= rhs;
    y *= rhs;
  }
  
  public Vector2 divide(double rhs) {
    return new Vector2(x/rhs, y/rhs);
  }
  
  public void over(double rhs) {
    x /= rhs;
    y /= rhs;
  }
  
  public void clampToLength(double maxL) {
    double magnitude = Math.sqrt(x*x + y*y);
    if (magnitude > maxL){
      x *= maxL/magnitude;
      y *= maxL/magnitude;
    }
  }
  
  public void setToLength(double newL) {
    double magnitude = Math.sqrt(x*x + y*y);
    x *= newL/magnitude;
    y *= newL/magnitude;
  }
  
  public void normalize() {
    double magnitude = Math.sqrt(x*x + y*y);
    x /= magnitude;
    y /= magnitude;
  }
  
  public Vector2 normalized() {
    double magnitude = Math.sqrt(x*x + y*y);
    return new Vector2(x/magnitude, y/magnitude);
  }
  
  public double distanceTo(Vector2 rhs) {
    double dx = rhs.x - x;
    double dy = rhs.y - y;
    return (double) Math.sqrt(dx*dx + dy*dy);
  }
  
  public Vector2 copy() {
    return new Vector2(x, y);
  }
  
  public boolean equals(Vector2 other) {
    return x == other.x && y == other.y;
  }
  
  public boolean isZero() {
    return x == 0 && y == 0;
  }
  
  public boolean exists() {
    return !(Double.isNaN(x) || Double.isNaN(y));
  }
  
  public double getAngle() {
    return Math.atan(y/x);
  }
  
  public static Vector2 interpolate(Vector2 a, Vector2 b, double t) {
    return a.plus((b.minus(a)).times(t));
  }
  
  public static double interpolate(double a, double b, double t) {
    return a + ((b-a)*t);
  }
  
  public static double dot(Vector2 a, Vector2 b) {
    return a.x*b.x + a.y*b.y;
  }
  
  public static Vector2 projAB(Vector2 a, Vector2 b) {
    return b.times(a.x*b.x + a.y*b.y);
  }
  
  public static Vector2 randomVector(double size) {
    Vector2 vec = new Vector2(
        Math.random() - .5f,
        Math.random() - .5f
    );
    vec.setToLength(size);
    return vec;
  }
  
  public static Vector2 zero() {
    return new Vector2(0, 0);
  }
  
  public static Vector2 rotate(Vector2 vector, double angle) {
    return new Vector2(
        vector.x * Math.cos(angle) - vector.y * Math.sin(angle),
        vector.x * Math.sin(angle) + vector.y * Math.cos(angle)
    );
  }
  
}
