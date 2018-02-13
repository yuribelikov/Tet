package bel.tetris.game;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class Cup extends Thread
{
  final static int STATE_GAME = 1;
  final static int STATE_PAUSED = 2;
  final static int STATE_DROPPING = 3;
  final static int STATE_GAME_OVER = 4;
  final static int STATE_NO_MORE_LEVELS = 5;
  final static int W = 10, H = 20;    // cup width and height
  private final static Point FIGURE_START_POS = new Point(3, -2);

  private final Painter painter;
  int state = STATE_GAME;
  int[][] contents = new int[H][W];
  Figure currentFigure = null, nextFigure = null;
  private int speed = 1;
  int level = 1, score = 0, prize = 0;
  String message = null;
  private boolean isAlive = true;


  Cup(Tetris tetris)
  {
    super();

    loadLevel();
    currentFigure = new Figure(FIGURE_START_POS);
    nextFigure = new Figure(FIGURE_START_POS);

    painter = new Painter(this, tetris);
    start();
  }

  public void run()
  {
    System.out.println("Cup is started.");
    try
    {
      while (isAlive)
      {
        painter.paint();
        if (state == STATE_DROPPING)
          sleepMs(10);
        else
        {
          int delay = 1000 - speed * 450;
          while (delay-- > 0 && state == STATE_GAME)    // dropping started or paused
            sleep(1);
        }

        currentFigure.pos.y++;
        if (isFigurePositionValid())      // end of normal loop
          continue;

        currentFigure.pos.y--;    // figure has reached a bottom shape
        merge();
        if (isLevelComplete())
          nextLevel();

        nextFigure();
        if (!isFigurePositionValid())
        {
          state = STATE_GAME_OVER;
          painter.paint();
          finishGame();
        }
        else if (state == STATE_DROPPING)
          state = STATE_GAME;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Cup is stopped.");
  }

  private boolean isFigurePositionValid()
  {
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
      {
        if (!currentFigure.getCurrContents()[y][x]) continue;
        int cx = currentFigure.pos.x + x;
        int cy = currentFigure.pos.y + y;
        if (cx < 0 || cx >= W) return false;
        if (cy >= H) return false;
        if (cy < 0) continue;
        if (contents[cy][cx] > 0) return false;
      }

    return true;
  }

  private void merge()
  {
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
      {
        if (!currentFigure.getCurrContents()[y][x]) continue;
        int cx = currentFigure.pos.x + x;
        int cy = currentFigure.pos.y + y;
        if (cx < 0 || cx >= W) continue;
        if (cy < 0 || cy >= H) continue;
        contents[cy][cx] = 1;
      }

    int mergedRowsQ = 0;
    boolean[] mergedRows = new boolean[H];
    for (int y = 0; y < H; y++)
    {
      mergedRows[y] = isRowComplete(y);
      if (mergedRows[y])
        mergedRowsQ++;
    }

    if (mergedRowsQ > 0)
    {
      prize = 100 * mergedRowsQ * mergedRowsQ * level * (speed + 1);
      painter.paint();
      sleepMs(500);
      score += prize;
      prize = 0;

      for (int y = 0; y < H; y++)
        if (mergedRows[y])
        {
          int[][] contents = new int[H][W];
          System.arraycopy(this.contents, 0, contents, 1, y);
          System.arraycopy(this.contents, y + 1, contents, y + 1, H - y - 1);
          this.contents = contents;
          for (int x = 0; x < W; x++) this.contents[0][x] = 0;
        }
    }

    painter.paint();
  }

  private void nextFigure()
  {
    currentFigure = nextFigure;
    nextFigure = new Figure(FIGURE_START_POS);
  }

  void finishGame()
  {
//    tetris.updateScores("", score, false);
    setStop();
  }

  private boolean isLevelComplete()
  {
    for (int y = 0; y < H; y++)
      for (int x = 0; x < W; x++)
        if (contents[y][x] > 1)
          return false;

    return true;
  }

  void pause()
  {
    if (state == STATE_GAME)
      state = Cup.STATE_PAUSED;
    else
      state = STATE_GAME;
  }

  void move(String action)
  {
    if (state != STATE_GAME)
      return;

    switch (action)
    {
      case "MoveLeft":
        currentFigure.pos.x--;
        if (!isFigurePositionValid()) currentFigure.pos.x++;
        break;
      case "MoveRight":
        currentFigure.pos.x++;
        if (!isFigurePositionValid()) currentFigure.pos.x--;
        break;
      case "Rotate":
        currentFigure.rotate();
        if (!isFigurePositionValid()) currentFigure.rotateBack();
        break;
      case "Drop":
        state = STATE_DROPPING;
        break;
    }

    painter.paint();
  }

  private void nextLevel() throws Exception
  {
    currentFigure = null;
    painter.paint();
    sleepMs(500);
    prize = 5000 * level * (speed + 1);
    message = "Bonus";
    painter.paint();
    sleepMs(1000);    // TODO: increase to 3000
    score += prize;
    prize = 0;
    message = null;
    painter.paint();
    sleepMs(500);
    level++;
    if (loadLevel())
      message = "Next Level: " + level;
    else
    {
      state = STATE_NO_MORE_LEVELS;
//              tetris.updateScores("", score, true);
    }
    painter.paint();
    sleepMs(2000);
    message = null;
  }

  boolean isRowComplete(int row)
  {
    for (int x = 0; x < W; x++)
      if (contents[row][x] == 0)
        return false;

    return true;
  }

  private boolean loadLevel()
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

  private void sleepMs(long ms)
  {
    try
    {
      while (ms-- > 0 && isAlive)
        sleep(1);

    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  void setStop()
  {
    isAlive = false;
  }
}
