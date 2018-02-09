package bel.tetris.game;

import java.awt.*;

class Figure
{
  String type = null;
  boolean[][][] contents = null;
  private int rotation = 0;
  Point location = null;

  boolean[][] getContentsForCurrRotation()
  {
    return contents[rotation];
  }

  void rotate()
  {
    rotation--;
    if (rotation < 0) rotation = 3;
  }

  void rotateBack()
  {
    rotation++;
    if (rotation > 3) rotation = 0;
  }

  public String toString()
  {
    return "Figure : " + type + " [" + 90 * rotation + "], " + location;
  }
}
