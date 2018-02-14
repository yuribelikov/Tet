package bel.tetris.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class Cup
{
  final static int W = 10, H = 20;    // cup width and height

  int[][] contents = new int[H][W];
  private int[][] mergeContents;


  Cup(int level)
  {
    loadLevel(level);
  }

  boolean isFigurePositionValid(Figure figure)
  {
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
      {
        if (!figure.getCurrContents()[y][x])
          continue;

        int cx = figure.pos.x + x;
        int cy = figure.pos.y + y;
        if (cx < 0 || cx >= W)
          return false;

        if (cy >= H)
          return false;

        if (cy < 0)
          continue;

        if (contents[cy][cx] > 0)
          return false;
      }

    return true;
  }

  int canMerge(Figure figure)
  {
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
      {
        if (!figure.getCurrContents()[y][x])
          continue;

        int cx = figure.pos.x + x;
        int cy = figure.pos.y + y;
        if (cx < 0 || cx >= W || cy < 0 || cy >= H)
          continue;

        contents[cy][cx] = 1;
      }

    mergeContents = new int[H][W];
    int mergedRows = 0;
    for (int y = 0; y < H; y++)
      if (isRowComplete(y))
      {   // for first merged row we copy from contents to mergeContents, for next merged rows if any we copy from mergeContents to mergeContents
        System.arraycopy(mergedRows == 0 ? contents : mergeContents, 0, mergeContents, 1, y);
        System.arraycopy(mergedRows == 0 ? contents : mergeContents, y + 1, mergeContents, y + 1, H - y - 1);
        mergedRows++;
      }

    return mergedRows;
  }

  void merge()
  {
    contents = mergeContents;
  }

  boolean isLevelComplete()
  {
    for (int y = 0; y < H; y++)
      for (int x = 0; x < W; x++)
        if (contents[y][x] > 1)
          return false;

    return true;
  }

  boolean isRowComplete(int row)
  {
    for (int x = 0; x < W; x++)
      if (contents[row][x] == 0)
        return false;

    return true;
  }

  boolean loadLevel(int level)
  {
    try
    {
      contents = new int[H][W];
      File f = new File(level + ".lvl");
      if (!f.exists()) return false;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      contents = (int[][]) ois.readObject();
      ois.close();

      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }

}
