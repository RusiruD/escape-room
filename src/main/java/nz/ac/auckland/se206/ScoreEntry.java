package nz.ac.auckland.se206;

public class ScoreEntry {
  public enum Movement {
    UP,
    DOWN,
    NONE
  }

  private Movement movement;

  private String difficulty;
  private int leaderboardPos;
  private String time;

  /**
   * Constructs a ScoreEntry object with the specified parameters.
   *
   * @param name The name associated with the score entry.
   * @param leaderboardPos The position of the entry on the leaderboard.
   * @param time The time taken to achieve the score.
   */
  public ScoreEntry(String difficulty, int leaderboardPos, String time) {
    this.difficulty = difficulty;
    this.leaderboardPos = leaderboardPos;
    this.time = time;
    movement = Movement.NONE; // Set the initial movement to NONE
  }

  public String getDifficulty() {
    return difficulty;
  }

  public String getTime() {
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
