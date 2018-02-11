package bel.tetris.game;

import lib.util.Log;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class Cup extends Thread
{
  private final static int STATE_GAME = 1;
  private final static int STATE_PAUSED = 2;
  private final static int STATE_DROPPING = 3;
  private final static int STATE_GAME_OVER = 4;
  private final static int STATE_NO_MORE_LEVELS = 5;

  private final static Color[] PALETTE = {null, new Color(222, 222, 222), Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
  private final static int W = 10, H = 20;    // cup width and height
  private final static Point FIGURE_START_POS = new Point(3, -2);

  private final Tetris tetris;
  private int[][] contents = new int[H][W];
  private Figure currentFigure = null, nextFigure = null;

  private int speed = 0, level = 1, score = 0, prize = 0;
  private int squareSize = 20;
  private Image bgImage = null;
  private String message = null;
  private int state;
  private boolean isAlive = true;


  Cup(Tetris tetris)
  {
    super();

    this.tetris = tetris;
    loadLevel();
    currentFigure = new Figure(FIGURE_START_POS);
    nextFigure = new Figure(FIGURE_START_POS);
    state = STATE_GAME;
    start();
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

    int completedRowsQ = 0;
    boolean[] completedRows = new boolean[H];
    for (int y = 0; y < H; y++)
    {
      boolean isRowComplete = true;
      for (int x = 0; x < W; x++)
      {
        if (contents[y][x] == 0)
        {
          isRowComplete = false;
          break;
        }
      }
      completedRows[y] = isRowComplete;
      if (isRowComplete) completedRowsQ++;
    }

    if (completedRowsQ > 0)
    {
      prize = 100 * completedRowsQ * completedRowsQ * level * (speed + 1);
      paint();
      sleepMs(500);
      score += prize;
      prize = 0;
      for (int y = 0; y < H; y++)
      {
        if (completedRows[y])
        {
          int[][] contents = new int[H][W];
          System.arraycopy(this.contents, 0, contents, 1, y);
          System.arraycopy(this.contents, y + 1, contents, y + 1, H - y - 1);
          this.contents = contents;
          for (int x = 0; x < W; x++) this.contents[0][x] = 0;
        }
      }
    }

    paint();
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

  public synchronized void paint()
  {
    Color cupColor = new Color(150, 150, 255);
    Color figureColor = new Color(180, 255, 180);
    Color mergeColor = new Color(255, 150, 255);
    Color scoreColor = new Color(220, 220, 255);
    Font scoreFont = new Font("Dialog", 1, 24);
    Color messageColor = new Color(255, 255, 150);
    Font messageFont = new Font("Dialog", 1, 60);
    Color InfoColor = new Color(220, 220, 240);
    Font InfoFont = new Font("Dialog", 1, 48);

    Dimension scrSize = tetris.getSize();
    Graphics g = bgImage.getGraphics();
    g.setColor(Color.black);
    g.fillRect(0, 0, scrSize.width, scrSize.height);

    g.setColor(cupColor);
    int w = squareSize * W;
    int h = squareSize * H;
    int x = scrSize.width / 4 - w / 2;
    int y = scrSize.height / 2 - h / 2 + 30;
    g.fillRect(x - 4, y, 4, h + 4);
    g.fillRect(x + w, y, 4, h + 4);
    g.fillRect(x, y + h, w, 4);

    paintContents(g, x, y, mergeColor);
    paintFigure(currentFigure, g, figureColor, x, y);
    paintFigure(nextFigure, g, figureColor, x + w, y + h / 2 - squareSize * 2);

    if (message != null)
      paintText(message, g, messageFont, messageColor, x + w / 2, y + 80);

    paintText("" + level, g, InfoFont, InfoColor, x + w + 50, y + 20);
    paintText(prize == 0 ? ("" + score) : (score + " + " + prize), g, scoreFont, scoreColor, x + w / 2, y + h);
    int mx = x + w / 2, my = y + h / 2 - 50;
    if (prize > 0)
      paintText("" + prize, g, messageFont, messageColor, mx, my);
    switch (state)
    {
      case STATE_PAUSED:
        paintText("PAUSED", g, messageFont, messageColor, mx, my);
        break;
      case STATE_GAME_OVER:
        paintText("GAME", g, messageFont, messageColor, mx, my);
        paintText("OVER", g, messageFont, messageColor, mx, my + 60);
        break;
      case STATE_NO_MORE_LEVELS:
        paintText("GET MORE", g, messageFont, messageColor, mx, my);
        paintText("LEVELS", g, messageFont, messageColor, mx, my + 60);
        break;
    }

    tetris.getGraphics().drawImage(bgImage, scrSize.width / 4, 0, tetris);
  }

  private void paintContents(Graphics _G, int _X, int _Y, Color mergeColor)
  {
    try
    {
      if (contents == null) return;

      for (int y = 0; y < H; y++)
      {
        boolean isRowComplete = true;
        for (int x = 0; x < W; x++)
        {
          if (contents[y][x] == 0)
          {
            isRowComplete = false;
            break;
          }
        }
        for (int x = 0; x < W; x++)
        {
          if (contents[y][x] > 0)
          {
            if (isRowComplete) _G.setColor(mergeColor);
            else _G.setColor(PALETTE[contents[y][x]]);
            _G.fillRect(_X + x * squareSize,
            _Y + y * squareSize,
            squareSize, squareSize);
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".paintContents() error : " + e);
    }
  }

  private void paintFigure(Figure figure, Graphics g, Color c, int fx, int fy)
  {
    if (figure == null) return;

    g.setColor(c);
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
        if (figure.getCurrContents()[y][x])
          g.fillRect(fx + (figure.pos.x + x) * squareSize, fy + (figure.pos.y + y) * squareSize, squareSize, squareSize);
  }

  private void paintText(String text, Graphics g, Font font, Color color, int x, int y)
  {
    g.setFont(font);
    g.setColor(color);
    int sw = g.getFontMetrics().stringWidth(text);
    int sh = g.getFontMetrics().getHeight();
    int sx = x - sw / 2;
    int sy = y + sh + 5;
    g.drawString(text, sx, sy);
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

    paint();
  }

  public void run()
  {
    System.out.println("Cup is started.");
    try
    {
      bgImage = tetris.createImage(tetris.getSize().width, tetris.getSize().height);

      paint();
      while (isAlive)
      {
//        if (!(state.equals("Game") || state.equals("Drop")))
//        {
//          paint();
//          sleep(50);
//          continue;
//        }

        if (!isFigurePositionValid())
        {
          currentFigure.pos.y--;
          merge();
          if (isLevelComplete())
            nextLevel();

          nextFigure();
          if (!isFigurePositionValid())
          {
            state = STATE_GAME_OVER;
            paint();
            sleep(2000);    // TODO: ???
//            tetris.updateScores("", score, true);
          }
          else state = STATE_GAME;      // TODO: ???
        }

        paint();
        if (state == STATE_DROPPING)
          sleepMs(10);
        else
        {
          int delay = 1000 - speed * 450;
          while (delay-- > 0 && state == STATE_GAME)    // dropping started or paused
            sleep(1);
        }

        currentFigure.pos.y++;
        paint();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Cup is stopped.");
  }

  private void nextLevel() throws Exception
  {
    currentFigure = null;
    paint();
    sleepMs(500);
    prize = 5000 * level * (speed + 1);
    message = "Bonus";
    paint();
    sleepMs(1000);    // TODO: increase to 3000
    score += prize;
    prize = 0;
    contents = null;
    message = null;
    paint();
    sleepMs(500);
    level++;
    if (loadLevel())
      message = "Next Level: " + level;
    else
    {
      state = STATE_NO_MORE_LEVELS;
//              tetris.updateScores("", score, true);
    }
    paint();
    sleepMs(2000);
    message = null;
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
