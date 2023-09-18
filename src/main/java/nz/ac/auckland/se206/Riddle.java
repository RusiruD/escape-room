package nz.ac.auckland.se206;

public class Riddle {
  private String question;
  private String riddle;
  private boolean hasRiddle = false;
  private DungeonMaster dungeonMaster;

  public Riddle(DungeonMaster dungeonMaster) {
    this.question = "Write me two lines of code that will print out the numbers 1 to 10";
    this.riddle = "42";
    this.dungeonMaster = dungeonMaster;
    // get the riddle from the dungeon master
    riddle = dungeonMaster.getRiddle(question);
    hasRiddle = true;
  }

  public DungeonMaster getDungeonMaster() {
    return dungeonMaster;
  }

  public boolean hasRiddle() {
    return hasRiddle;
  }
}
