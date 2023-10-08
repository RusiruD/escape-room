package nz.ac.auckland.se206;

/**
 * The ScoreEntry class represents a score entry in the leaderboard. It includes information such as
 * player name, score, and movements made during the game.
 */
public class ScoreEntry {
  /** Enum representing different types of movements. */
  public enum Movement {
    UP,
    DOWN,
    NONE
  }

  private Movement movement;

  // keystones being quickness, accuracy, memory, creativity, spatial awareness
  private double[] statPoints;

  private String name;
  private int leaderboardPos;
  private int time;

  /**
   * Constructs a ScoreEntry object with the specified parameters.
   *
   * @param name The name associated with the score entry.
   * @param leaderboardPos The position of the entry on the leaderboard.
   * @param time The time taken to achieve the score.
   * @param statPoints An array of statistical points associated with the score entry.
   */
  public ScoreEntry(String name, int leaderboardPos, int time, double[] statPoints) {
    this.name = name;
    this.leaderboardPos = leaderboardPos;
    this.time = time;
    this.statPoints = statPoints;
    movement = Movement.NONE; // Set the initial movement to NONE
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
