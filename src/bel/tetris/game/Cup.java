package bel.tetris.game;

import lib.util.Log;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class Cup extends Thread
{
  private final static String FIGURES[] = {"LStair", "RStair", "Podium", "LCorner", "RCorner", "Square", "Line"};
  private final static Color[] PALETTE = {null, new Color(222, 222, 222), Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
  private final static int W = 10, H = 20, F = 4;    // cup width and height, figure width/height
  private final static Point START_LOCATION = new Point(3, -2);

  private final Tetris tetris;
  private int[][] contents = new int[H][W];
  private Figure currentFigure = null, nextFigure = null;

  private int[] lastFigures = null;
  private int speed = 0, level = 1, score = 0, prize = 0;
  private int squareSize = 20;
  private Image bgImage = null;
  private String message = null;
  private String state = "Idle", prevState = null;
  private boolean isAlive = true;


  Cup(Tetris tetris)
  {
    super();

    this.tetris = tetris;
    lastFigures = new int[2];
    setState("Idle");
    start();
  }

  void newGame()
  {
    loadLevel();
    nextFigure = generateFigure();
    cfNext();

    setState("Game");
  }

  private boolean cfIsValid()
  {
    try
    {
      for (int y = 0; y < currentFigure.getContentsForCurrRotation().length; y++)
        for (int x = 0; x < currentFigure.getContentsForCurrRotation()[0].length; x++)
        {
          if (!currentFigure.getContentsForCurrRotation()[y][x]) continue;
          int cx = currentFigure.location.x + x;
          int cy = currentFigure.location.y + y;
          if (cx < 0 || cx >= W) return false;
          if (cy >= H) return false;
          if (cy < 0) continue;
          if (contents[cy][cx] > 0) return false;
        }

      return true;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".cfIsValid() error : " + e);
    }

    return false;
  }

  private void cfMerge()
  {
    try
    {
      for (int y = 0; y < currentFigure.getContentsForCurrRotation().length; y++)
        for (int x = 0; x < currentFigure.getContentsForCurrRotation()[0].length; x++)
        {
          if (!currentFigure.getContentsForCurrRotation()[y][x]) continue;
          int cx = currentFigure.location.x + x;
          int cy = currentFigure.location.y + y;
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
        sleep(500);
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
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void cfNext()
  {
    currentFigure = nextFigure;
    currentFigure.location.x = START_LOCATION.x;
    currentFigure.location.y = START_LOCATION.y;
    nextFigure = generateFigure();
  }

  void finishGame()
  {
//    tetris.updateScores("", score, false);
    setStop();
  }

  private Figure generateFigure()
  {
    int type = -1;
    while (type < 0 || (type == lastFigures[0] && type == lastFigures[1]))
      type = (int) Math.round((FIGURES.length - 1) * Math.random());

    lastFigures[0] = lastFigures[1];
    lastFigures[1] = type;
    Figure figure = new Figure();
    figure.type = FIGURES[type];
    figure.type = "Line";
    figure.location = new Point();
    figure.contents = new boolean[4][F][F];

    switch (figure.type)
    {
      case "LStair":
        figure.contents[0][1][2] = figure.contents[0][1][3] =                //  xx
                figure.contents[0][2][1] = figure.contents[0][2][2] = true;            // xx


        figure.contents[1][1][1] =                                          // x
                figure.contents[1][2][1] = figure.contents[1][2][2] =                // xx
                        figure.contents[1][3][2] = true;                                    //  x


        figure.contents[2][1][2] = figure.contents[2][1][3] =                //  xx
                figure.contents[2][2][1] = figure.contents[2][2][2] = true;            // xx


        figure.contents[3][1][1] =                                          // x
                figure.contents[3][2][1] = figure.contents[3][2][2] =                // xx
                        figure.contents[3][3][2] = true;                                    //  x

        break;
      case "RStair":
        figure.contents[0][1][0] = figure.contents[0][1][1] =                // xx
                figure.contents[0][2][1] = figure.contents[0][2][2] = true;            //  xx


        figure.contents[1][1][2] =                                          //  x
                figure.contents[1][2][1] = figure.contents[1][2][2] =                // xx
                        figure.contents[1][3][1] = true;                                    // x


        figure.contents[2][1][0] = figure.contents[2][1][1] =                // xx
                figure.contents[2][2][1] = figure.contents[2][2][2] = true;            //  xx


        figure.contents[3][1][2] =                                          //  x
                figure.contents[3][2][1] = figure.contents[3][2][2] =                // xx
                        figure.contents[3][3][1] = true;                                    // x

        break;
      case "Podium":
        figure.contents[0][1][2] =                                          //   x
                figure.contents[0][2][1] = figure.contents[0][2][2] =                //  xx
                        figure.contents[0][3][2] = true;                                    //   x


        figure.contents[1][1][1] =                                                              //  x
                figure.contents[1][2][0] = figure.contents[1][2][1] = figure.contents[1][2][2] = true;      // xxx


        figure.contents[2][1][1] =                                          //  x
                figure.contents[2][2][1] = figure.contents[2][2][2] =                //  xx
                        figure.contents[2][3][1] = true;                                    //  x


        figure.contents[3][1][0] = figure.contents[3][1][1] = figure.contents[3][1][2] =            // xxx
                figure.contents[3][2][1] = true;                                                        //  x

        break;
      case "LCorner":
        figure.contents[0][1][1] = figure.contents[0][1][2] =                //  xx
                figure.contents[0][2][2] =                                          //   x
                        figure.contents[0][3][2] = true;                                    //   x


        figure.contents[1][1][2] =                                                              //   x
                figure.contents[1][2][0] = figure.contents[1][2][1] = figure.contents[1][2][2] = true;      // xxx


        figure.contents[2][1][1] =                                          //  x
                figure.contents[2][2][1] =                                          //  x
                        figure.contents[2][3][1] = figure.contents[2][3][2] = true;            //  xx


        figure.contents[3][1][0] = figure.contents[3][1][1] = figure.contents[3][1][2] =            // xxx
                figure.contents[3][2][0] = true;                                                        // x

        break;
      case "RCorner":
        figure.contents[0][1][1] = figure.contents[0][1][2] =                //  xx
                figure.contents[0][2][1] =                                          //  x
                        figure.contents[0][3][1] = true;                                    //  x


        figure.contents[1][1][0] = figure.contents[1][1][1] = figure.contents[1][1][2] =            // xxx
                figure.contents[1][2][2] = true;                                                        //   x


        figure.contents[2][1][2] =                                          //   x
                figure.contents[2][2][2] =                                          //   x
                        figure.contents[2][3][1] = figure.contents[2][3][2] = true;            //  xx


        figure.contents[3][1][0] =                                                              // x
                figure.contents[3][2][0] = figure.contents[3][2][1] = figure.contents[3][2][2] = true;      // xxx

        break;
      case "Square":
        for (int n = 0; n < 4; n++)
        {
          figure.contents[n][1][1] = figure.contents[n][1][2] =                                      //  xx
                  figure.contents[n][2][1] = figure.contents[n][2][2] = true;                                  //  xx
        }
        break;
      case "Line":
        figure.contents[0][2][0] = figure.contents[0][2][1] = figure.contents[0][2][2] = figure.contents[0][2][3] = true;    //  xxxx


        figure.contents[1][0][2] =                                                            //  x
                figure.contents[1][1][2] =                                                            //  x
                        figure.contents[1][2][2] =                                                            //  x
                                figure.contents[1][3][2] = true;                                                      //  x


        figure.contents[2][2][0] = figure.contents[2][2][1] = figure.contents[2][2][2] = figure.contents[2][2][3] = true;    //  xxxx


        figure.contents[3][0][2] = true;                                                            //  x
        figure.contents[3][1][2] = true;                                                            //  x
        figure.contents[3][2][2] = true;                                                            //  x
        figure.contents[3][3][2] = true;                                                            //  x

        break;
    }

    return figure;
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
    g.fillRect(0, 0, scrSize.width / 2, scrSize.height);

    if (!state.equals("Idle"))
    {
      g.setColor(cupColor);
      int w = squareSize * W;
      int h = squareSize * H;
      int x = scrSize.width / 4 - w / 2;
      int y = scrSize.height / 2 - h / 2 + 30;
      g.fillRect(x - 4, y, 4, h + 4);
      g.fillRect(x + w, y, 4, h + 4);
      g.fillRect(x, y + h, w, 4);

      paintFigure(nextFigure, g, figureColor, x + w + 10, y + h / 2 - squareSize * 2);
      paintContents(g, x, y, mergeColor);
      paintFigure(currentFigure, g, figureColor, x, y);

      if (message != null)
        paintText(message, g, messageFont, messageColor, x + w / 2, y + 80);

      paintText("" + level, g, InfoFont, InfoColor, x + w + 50, y + 20);
      paintText(prize == 0 ? ("" + score) : (score + " + " + prize), g, scoreFont, scoreColor, x + w / 2, y + h);
      int mx = x + w / 2, my = y + h / 2 - 50;
      if (prize > 0)
        paintText("" + prize, g, messageFont, messageColor, mx, my);
      switch (state)
      {
        case "Pause":
          paintText("�����", g, messageFont, messageColor, mx, my);
          break;
        case "Over":
          paintText("��", g, messageFont, messageColor, mx, my);
          paintText("���������", g, messageFont, messageColor, mx, my + 60);
          break;
        case "Victory":
          paintText("��", g, messageFont, messageColor, mx, my);
          paintText("��������", g, messageFont, messageColor, mx, my + 60);
          break;
      }
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

  private void paintFigure(Figure _Figure, Graphics _G, Color _C, int _X, int _Y)
  {
    if (_Figure == null) return;

    _G.setColor(_C);
    for (int y = 0; y < _Figure.getContentsForCurrRotation().length; y++)
      for (int x = 0; x < _Figure.getContentsForCurrRotation()[0].length; x++)
      {
        if (_Figure.getContentsForCurrRotation()[y][x])
        {
          _G.fillRect(_X + (_Figure.location.x + x) * squareSize,
                  _Y + (_Figure.location.y + y) * squareSize,
                  squareSize, squareSize);
        }
      }
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

  void processAction(String action)
  {
    if (action.equals("No"))
    {
      state = prevState;
    }
    else if (action.equals("Pause") && state.equals("Game"))
    {
      setState("Pause");
    }
    else if (action.equals("Pause") && state.equals("Pause"))
    {
      setState("Game");
    }
    else if (action.equals("Quit") && state.equals("Game"))
    {
      setState("Pause");
    }
    else if (action.equals("Quit") && !state.equals("Game"))
    {
      setState(state);
    }
    else if (action.equals("MoveLeft") && state.equals("Game"))
    {
      currentFigure.location.x--;
      if (!cfIsValid()) currentFigure.location.x++;
    }
    else if (action.equals("MoveRight") && state.equals("Game"))
    {
      currentFigure.location.x++;
      if (!cfIsValid()) currentFigure.location.x--;
    }
    else if (action.equals("Rotate") && state.equals("Game"))
    {
      currentFigure.rotate();
      if (!cfIsValid()) currentFigure.rotateBack();
    }
    else if (action.equals("Drop") && state.equals("Game"))
    {
      setState("Drop");
    }
  }

  public void run()
  {
    System.out.println("Cup is started.");
    try
    {
      bgImage = tetris.createImage(tetris.getSize().width / 2, tetris.getSize().height);

      while (isAlive)
      {
        if (!(state.equals("Game") || state.equals("Drop")))
        {
          paint();
          sleep(50);
          continue;
        }

        if (!cfIsValid())
        {
          currentFigure.location.y--;
          cfMerge();
          if (isLevelComplete())
            nextLevel();

          cfNext();
          if (!cfIsValid())
          {
            setState("Over");
            paint();
            sleep(2000);
//            tetris.updateScores("", score, true);
          }
          else setState("Game");
        }

        paint();

        if (state.equals("Drop")) sleep(10);
        else
        {
          int delay = 0;
          switch (speed)
          {
            case 0:
              delay = 1500;
              break;
            case 1:
              delay = 900;
              break;
            case 2:
              delay = 500;
              break;
            case 3:
              delay = 200;
              break;
            case 4:
              delay = 80;
              break;
          }
          for (int n = 0; n < delay; n++)
          {
            if (!state.equals("Game")) break;
            sleep(1);
            paint();
          }
        }

        currentFigure.location.y++;
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
    sleep(500);
    prize = 5000 * level * (speed + 1);
    message = "Bonus";
    paint();
    sleep(1000);    // TODO: increase to 3000
    score += prize;
    prize = 0;
    contents = null;
    message = null;
    paint();
    sleep(500);
    level++;
    if (loadLevel())
      message = "Next Level: " + level;
    else
    {
      setState("Victory");
//              tetris.updateScores("", score, true);
    }
    paint();
    sleep(2000);
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

  private void setState(String newState)
  {
    prevState = state;
    state = newState;
  }

  void setStop()
  {
    isAlive = false;
  }
}
