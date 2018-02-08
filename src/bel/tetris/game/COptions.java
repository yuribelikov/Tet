package bel.tetris.game;

public class COptions
{
  public String[] actions = null;
  public String[] keys = null;

  public COptions()
  {
    actions = new String[4];
    keys = new String[4];
    actions[0] = "MoveLeft";
    actions[1] = "MoveRight";
    actions[2] = "Rotate";
    actions[3] = "Drop";
    keys[0] = "Left";
    keys[1] = "Right";
    keys[2] = "Up";
    keys[3] = "Down";
  }

}
