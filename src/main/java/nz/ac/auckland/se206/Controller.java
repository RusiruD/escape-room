package nz.ac.auckland.se206;

public interface Controller {
  public static Controller getInstance() {
    return null;
  }

  public void updateTimerLabel(String time);

  public void updateInventory();
}
