package nz.ac.auckland.se206;

public class ScoreEntry {
  public enum Movement {UP, DOWN, NONE};

  private Movement movement;
  
  private String name;
  private int leaderboardPos;
  private int time;

  public ScoreEntry(String name, int leaderboardPos, int time) {
    this.name = name;
    this.leaderboardPos = leaderboardPos;
    this.time = time;
    movement = movement.NONE;
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

  public void setLeaderboardPos(int leaderboardPos) {
    this.leaderboardPos = leaderboardPos;
  }

  public void setMovement(Movement movement) {
    this.movement = movement;
  }
}
