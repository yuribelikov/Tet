package bel.tetris.game;

import lib.util.Log;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class CCup extends Thread
{
  private Tetris tetris = null;

  private int W = 10, H = 20, F = 4;    // cup width and height, figure width/height
  private int[][] Contents = null;
  private String FigureTypes[] = {"LStair", "RStair", "Podium", "LCorner", "RCorner", "Square", "Line"};
  private Figure CurrentFigure = null, NextFigure = null;
  private int[] PrevFigureTypes = null;
  private int PlayerN = 1, Speed = 0, Level = 0, Score = 0, Prize = 0;
  private boolean IsNextFigureShowed = false;
  private String PlayerName = null;

  private int SquareSize = 20;
  private Image BgImage = null;
  private Point StartLocation = null;
  private Color[] Palette = {null, new Color(222, 222, 222), Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
  private Color CupColor = null, CurrentFigureColor = null, NextFigureColor = null,
          MergeColor = null, ScoreColor = null, MessageColor = null, InfoColor = null, PlayerNameColor = null;
  private Font ScoreFont = null, MessageFont = null, InfoFont = null, PlayerNameFont = null;
  private String Text = null;

  private String State = null, PrevState = null;
  private int DropDelay = 10, MergeDelay = 500;
  private boolean IsAlive = true, IsShouldBeRepainted = false;


  CCup(Tetris tetris)
  {
    super();

    this.tetris = tetris;
    PrevFigureTypes = new int[2];
    StartLocation = new Point(3, -2);
    setState("Idle");

    CupColor = new Color(150, 150, 255);
    CurrentFigureColor = new Color(180, 255, 180);
    NextFigureColor = new Color(255, 100, 100);
    MergeColor = new Color(255, 150, 255);
    ScoreColor = new Color(220, 220, 255);
    ScoreFont = new Font("Dialog", 1, 24);
    MessageColor = new Color(255, 255, 150);
    MessageFont = new Font("Dialog", 1, 60);
    InfoColor = new Color(220, 220, 240);
    InfoFont = new Font("Dialog", 1, 48);
    PlayerNameColor = new Color(240, 240, 255);
    PlayerNameFont = new Font("Dialog", 1, 32);
  }

  private boolean cfIsValid()
  {
    try
    {
      for (int y = 0; y < CurrentFigure.getContentsForCurrRotation().length; y++)
        for (int x = 0; x < CurrentFigure.getContentsForCurrRotation()[0].length; x++)
        {
          if (!CurrentFigure.getContentsForCurrRotation()[y][x]) continue;
          int cx = CurrentFigure.location.x + x;
          int cy = CurrentFigure.location.y + y;
          if (cx < 0 || cx >= W) return false;
          if (cy >= H) return false;
          if (cy < 0) continue;
          if (Contents[cy][cx] > 0) return false;
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
      for (int y = 0; y < CurrentFigure.getContentsForCurrRotation().length; y++)
        for (int x = 0; x < CurrentFigure.getContentsForCurrRotation()[0].length; x++)
        {
          if (!CurrentFigure.getContentsForCurrRotation()[y][x]) continue;
          int cx = CurrentFigure.location.x + x;
          int cy = CurrentFigure.location.y + y;
          if (cx < 0 || cx >= W) continue;
          if (cy < 0 || cy >= H) continue;
          Contents[cy][cx] = 1;
        }

      int completedRowsQ = 0;
      boolean[] completedRows = new boolean[H];
      for (int y = 0; y < H; y++)
      {
        boolean isRowComplete = true;
        for (int x = 0; x < W; x++)
        {
          if (Contents[y][x] == 0)
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
        Prize = 100 * completedRowsQ * completedRowsQ * Level * (Speed + 1);
        paint();
        sleep(MergeDelay);
        Score += Prize;
        Prize = 0;
        for (int y = 0; y < H; y++)
        {
          if (completedRows[y])
          {
            int[][] contents = new int[H][W];
            System.arraycopy(Contents, 0, contents, 1, y);
            System.arraycopy(Contents, y + 1, contents, y + 1, H - y - 1);
            Contents = contents;
            for (int x = 0; x < W; x++) Contents[0][x] = 0;
          }
        }
      }

      paint();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".cfMerge() error : " + e);
    }
  }

  private void cfNext()
  {
    CurrentFigure = NextFigure;
    CurrentFigure.location.x = StartLocation.x;
    CurrentFigure.location.y = StartLocation.y;
    NextFigure = generateFigure();
  }

  public void finishGame()
  {
    tetris.updateScores(PlayerName, Score, false);
  }

  private Figure generateFigure()
  {
    try
    {
      int type = -1;
      while (type < 0 || (type == PrevFigureTypes[0] && type == PrevFigureTypes[1]))
        type = (int) Math.round((FigureTypes.length - 1) * Math.random());

      PrevFigureTypes[0] = PrevFigureTypes[1];
      PrevFigureTypes[1] = type;
      Figure figure = new Figure();
      figure.type = FigureTypes[type];
//figure.type="Line";
      figure.location = new Point();
      figure.contents = new boolean[4][F][F];

      if (figure.type.equals("LStair"))
      {
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
      }
      else if (figure.type.equals("RStair"))
      {
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
      }

      else if (figure.type.equals("Podium"))
      {
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
      }

      else if (figure.type.equals("LCorner"))
      {
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
      }
      else if (figure.type.equals("RCorner"))
      {
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
      }

      else if (figure.type.equals("Square"))
      {
        for (int n = 0; n < 4; n++)
        {
          figure.contents[n][1][1] = figure.contents[n][1][2] =                                      //  xx
                  figure.contents[n][2][1] = figure.contents[n][2][2] = true;                                  //  xx
        }
      }

      else if (figure.type.equals("Line"))
      {
        figure.contents[0][2][0] = figure.contents[0][2][1] = figure.contents[0][2][2] = figure.contents[0][2][3] = true;    //  xxxx

        figure.contents[1][0][2] =                                                            //  x
                figure.contents[1][1][2] =                                                            //  x
                        figure.contents[1][2][2] =                                                            //  x
                                figure.contents[1][3][2] = true;                                                      //  x

        figure.contents[2][2][0] = figure.contents[2][2][1] = figure.contents[2][2][2] = figure.contents[2][2][3] = true;    //  xxxx

        figure.contents[3][0][2] =                                                            //  x
                figure.contents[3][1][2] =                                                            //  x
                        figure.contents[3][2][2] =                                                            //  x
                                figure.contents[3][3][2] = true;                                                      //  x
      }

      Log.log(figure);
      return figure;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".generateFigure() error : " + e);
    }

    return null;
  }

  private boolean isLevelComplete()
  {
    try
    {
      for (int y = 0; y < H; y++)
        for (int x = 0; x < W; x++)
          if (Contents[y][x] > 1) return false;

      return true;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".isLevelComplete() error : " + e);
    }

    return false;
  }

  private boolean loadLevel()
  {
    try
    {
      Contents = new int[H][W];
      File f = new File(Level + ".lvl");
      if (!f.exists()) return false;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      Contents = (int[][]) ois.readObject();
      ois.close();

      return true;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".loadLevel() error : " + e);
    }
    return false;
  }

  public void newGame(java.util.Hashtable _Data)
  {
    try
    {
      Contents = new int[H][W];
      CurrentFigure = NextFigure = null;
      Level = 1;
      Score = 0;
      setState("Idle");

      PlayerName = (String) _Data.get("Player" + PlayerN + "Name");
      Speed = (int) Integer.parseInt((String) _Data.get("Speed"));
      IsNextFigureShowed = _Data.get("IsNextFigureShowed").equals("true");
      loadLevel();
      NextFigure = generateFigure();
      cfNext();

      setState("Game");
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".newGame() error : " + e);
    }
  }

  public synchronized void paint()
  {
    try
    {
      Dimension scrSize = tetris.getSize();

      Graphics g = BgImage.getGraphics();
      g.setColor(Color.black);
      g.fillRect(0, 0, scrSize.width / 2, scrSize.height);

      if (!State.equals("Idle"))
      {
        g.setColor(CupColor);
        int w = SquareSize * W;
        int h = SquareSize * H;
        int x = scrSize.width / 4 - w / 2;
        int y = scrSize.height / 2 - h / 2 + 30;
        g.fillRect(x - 4, y, 4, h + 4);
        g.fillRect(x + w, y, 4, h + 4);
        g.fillRect(x, y + h, w, 4);

        if (IsNextFigureShowed)
          paintFigure(NextFigure, g, NextFigureColor, x + w + 10, y + h / 2 - SquareSize * 2);
        paintContents(g, x, y);
        paintFigure(CurrentFigure, g, CurrentFigureColor, x, y);

        paintText(Text, g, MessageFont, MessageColor, x + w / 2, y + 80);
        paintText(PlayerName, g, PlayerNameFont, PlayerNameColor, x + w / 2, y - 65);
        paintText("" + Level, g, InfoFont, InfoColor, x + w + 50, y + 20);
        paintText(Prize == 0 ? ("" + Score) : (Score + " + " + Prize), g, ScoreFont, ScoreColor, x + w / 2, y + h);
        int mx = x + w / 2, my = y + h / 2 - 50;
        if (Prize > 0) paintText("" + Prize, g, MessageFont, MessageColor, mx, my);
        if (State.equals("Pause")) paintText("�����", g, MessageFont, MessageColor, mx, my);
        else if (State.equals("Over"))
        {
          paintText("��", g, MessageFont, MessageColor, mx, my);
          paintText("���������", g, MessageFont, MessageColor, mx, my + 60);
        }
        else if (State.equals("Victory"))
        {
          paintText("��", g, MessageFont, MessageColor, mx, my);
          paintText("��������", g, MessageFont, MessageColor, mx, my + 60);
        }
      }

      g = tetris.getGraphics();
      int x = 0;
      x = scrSize.width / 4;
      synchronized (g)
      {
        g.drawImage(BgImage, x, 0, tetris);
      }
      IsShouldBeRepainted = false;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".paint() error : " + e);
    }
  }

  private void paintContents(Graphics _G, int _X, int _Y)
  {
    try
    {
      if (Contents == null) return;

      for (int y = 0; y < H; y++)
      {
        boolean isRowComplete = true;
        for (int x = 0; x < W; x++)
        {
          if (Contents[y][x] == 0)
          {
            isRowComplete = false;
            break;
          }
        }
        for (int x = 0; x < W; x++)
        {
          if (Contents[y][x] > 0)
          {
            if (isRowComplete) _G.setColor(MergeColor);
            else _G.setColor(Palette[Contents[y][x]]);
            _G.fillRect(_X + x * SquareSize,
                    _Y + y * SquareSize,
                    SquareSize, SquareSize);
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
    try
    {
      if (_Figure == null) return;

      _G.setColor(_C);
      for (int y = 0; y < _Figure.getContentsForCurrRotation().length; y++)
        for (int x = 0; x < _Figure.getContentsForCurrRotation()[0].length; x++)
        {
          if (_Figure.getContentsForCurrRotation()[y][x])
          {
            _G.fillRect(_X + (_Figure.location.x + x) * SquareSize,
                    _Y + (_Figure.location.y + y) * SquareSize,
                    SquareSize, SquareSize);
          }
        }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".paintFigure() error : " + e);
    }
  }

  private void paintText(String _Text, Graphics _G, Font _Font, Color _Color, int _X, int _Y)
  {
    if (_Text == null) return;

    _G.setFont(_Font);
    _G.setColor(_Color);
    int sw = _G.getFontMetrics().stringWidth(_Text);
    int sh = _G.getFontMetrics().getHeight();
    int sx = _X - sw / 2;
    int sy = _Y + sh + 5;
    _G.drawString(_Text, sx, sy);
  }

  void processAction(String action)
  {
    try
    {
      if (action.equals("No"))
      {
        State = PrevState;
      }
      else if (action.equals("Pause") && State.equals("Game"))
      {
        setState("Pause");
      }
      else if (action.equals("Pause") && State.equals("Pause"))
      {
        setState("Game");
      }
      else if (action.equals("Quit") && State.equals("Game"))
      {
        setState("Pause");
      }
      else if (action.equals("Quit") && !State.equals("Game"))
      {
        setState(State);
      }
      else if (action.equals("MoveLeft") && State.equals("Game"))
      {
        CurrentFigure.location.x--;
        if (!cfIsValid()) CurrentFigure.location.x++;
        else IsShouldBeRepainted = true;
      }
      else if (action.equals("MoveRight") && State.equals("Game"))
      {
        CurrentFigure.location.x++;
        if (!cfIsValid()) CurrentFigure.location.x--;
        else IsShouldBeRepainted = true;
      }
      else if (action.equals("Rotate") && State.equals("Game"))
      {
        CurrentFigure.rotate();
        if (!cfIsValid()) CurrentFigure.rotateBack();
        else IsShouldBeRepainted = true;
      }
      else if (action.equals("Drop") && State.equals("Game"))
      {
        setState("Drop");
      }

    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".processAction() error : " + e);
    }
  }

  public void run()
  {
    Log.log(getClass().getName() + " is started.");
    try
    {
      while (BgImage == null)
      {
        sleep(100);
        BgImage = tetris.createImage(tetris.getSize().width / 2, tetris.getSize().height);
      }

      while (IsAlive)
      {
        if (!(State.equals("Game") || State.equals("Drop")))
        {
          paint();
          sleep(50);
          continue;
        }

        if (!cfIsValid())
        {
          CurrentFigure.location.y--;
          cfMerge();
          if (isLevelComplete())
          {
            CurrentFigure = null;
            paint();
            sleep(500);
            Prize = 5000 * Level * (Speed + 1);
            Text = "����";
            paint();
            sleep(3000);
            Score += Prize;
            Prize = 0;
            Contents = null;
            Text = null;
            paint();
            sleep(500);
            Level++;
            if (loadLevel())
            {
              Text = "������� " + Level;
            }
            else
            {
              setState("Victory");
              tetris.updateScores(PlayerName, Score, true);
              continue;
            }
            paint();
            sleep(2000);
            Text = null;
          }
          cfNext();
          if (!cfIsValid())
          {
            setState("Over");
            paint();
            sleep(2000);
            tetris.updateScores(PlayerName, Score, true);
          }
          else setState("Game");
        }

        paint();

        if (State.equals("Drop")) sleep(DropDelay);
        else
        {
          int delay = 0;
          switch (Speed)
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
            if (!State.equals("Game")) break;
            sleep(1);
            if (IsShouldBeRepainted) paint();
          }
        }

        CurrentFigure.location.y++;
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".run() error : " + e);
    }
    Log.log(getClass().getName() + " is stopped.");
  }

  private void setState(String _NewState)
  {
    PrevState = State;
    State = _NewState;
    IsShouldBeRepainted = true;
  }

  public void setStop()
  {
    IsAlive = false;
  }
}
