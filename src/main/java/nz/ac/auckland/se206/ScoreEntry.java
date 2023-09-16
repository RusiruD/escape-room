package nz.ac.auckland.se206;

public class ScoreEntry {
  public enum Movement {UP, DOWN, NONE};

  private Movement movement;

  //keystones being quickness, accuracy, memory, creativity, spatial awareness
  private double[] statPoints;

  private String name;
  private int leaderboardPos;
  private int time;

  public ScoreEntry(String name, int leaderboardPos, int time, double[] statPoints) {
    this.name = name;
    this.leaderboardPos = leaderboardPos;
    this.time = time;
    this.statPoints = statPoints;
    movement = Movement.NONE;
  }
  
  public String getName() {
    return name;
  }

  public int getTime() {
    return time;
  }

  public int getLeaderboardPos() {
    return leaderboardPos;
  }

  public Movement getMovement() {
    return movement;
  }

  public double[] getStatPoints() {
    return statPoints;
  }

  public void setLeaderboardPos(int leaderboardPos) {
    this.leaderboardPos = leaderboardPos;
  }

  public void setMovement(Movement movement) {
    this.movement = movement;
  }
}
