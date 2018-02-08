package bel.tetris.editor;

import bel.tetris.event.CEvent;
import lib.util.Log;

import java.awt.*;
import java.io.*;

public class CCup extends Thread
{
  private Runner Runner = null;

  private int W = 10, H = 20;    // cup width and height
  private int[][] Contents = null;
  private int Level = 0, SelectedColor = 1;

  private int SquareSize = 20;
  private Image BgImage = null;
  private Point PaletteLocation = null;
  private Color[] Palette = {null, null, Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
  private Color CupColor = null, InfoColor = null;
  private Color[] GridColor = {Color.darkGray, Color.gray};
  private Font InfoFont = null;

  private String Task = null;
  private boolean IsAlive = true;

  public CCup(Runner _Runner)
  {
    super();

    Runner = _Runner;
    PaletteLocation = new Point(-2, 4);
    CupColor = new Color(150, 150, 255);
    InfoColor = new Color(220, 220, 240);
    InfoFont = new Font("Dialog", 1, 48);
  }

  private void loadLevel()
  {
    try
    {
      Contents = new int[H][W];
      File f = new File(Level + ".lvl");
      if (!f.exists()) return;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      Contents = (int[][]) ois.readObject();
      ois.close();

    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".loadLevel() error : " + e);
    }
  }

  private void mousePressed(int _X, int _Y, boolean _RightButton)
  {
    try
    {
      Dimension scrSize = Runner.getSize();
      int w = SquareSize * W;
      int h = SquareSize * H;
      int x = scrSize.width / 2 - w / 2;
      int y = scrSize.height / 2 - h / 2 + 10;

      x = (int) Math.floor((_X - x) / (double) SquareSize);
      y = (_Y - y) / SquareSize;
      if (x >= 0 && x < W && y >= 0 && y < H)
      {
        Contents[y][x] = _RightButton ? 0 : SelectedColor;
      }
      else
      {
        y -= PaletteLocation.y;
        if (x == PaletteLocation.x && y > 1 && y < Palette.length) SelectedColor = y;
      }

      paint();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".mousePressed() error : " + e);
    }
  }

  public synchronized void paint()
  {
    try
    {
      if (BgImage == null) return;

      Dimension scrSize = Runner.getSize();
      Graphics g = BgImage.getGraphics();
      g.setColor(Color.black);
      g.fillRect(0, 0, scrSize.width, scrSize.height);

      if (Level > 0)
      {
        g.setColor(CupColor);
        int w = SquareSize * W;
        int h = SquareSize * H;
        int x = scrSize.width / 2 - w / 2;
        int y = scrSize.height / 2 - h / 2 + 10;
        g.fillRect(x - 4, y, 4, h + 4);
        g.fillRect(x + w, y, 4, h + 4);
        g.fillRect(x, y + h, w, 4);

        g.setColor(Palette[SelectedColor]);
        g.fillRect(x - 60, y, 2 * SquareSize, 2 * SquareSize);

        for (int n = 2; n < Palette.length; n++)
        {
          g.setColor(Palette[n]);
          g.fillRect(x + PaletteLocation.x * SquareSize,
                  y + (PaletteLocation.y + n) * SquareSize,
                  SquareSize, SquareSize);
        }
        paintContents(g, x, y);
        paintText("" + Level, g, InfoFont, InfoColor, x + w + 50, y + 20);
      }

      g = Runner.getGraphics();
      g.drawImage(BgImage, 0, 0, Runner);
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

      boolean gridSwitcher = true;
      for (int y = 0; y < H; y++)
      {
        for (int x = 0; x < W; x++)
        {
          if (Contents[y][x] > 0) _G.setColor(Palette[Contents[y][x]]);
          else _G.setColor(GridColor[gridSwitcher ? 0 : 1]);
          _G.fillRect(_X + x * SquareSize,
                  _Y + y * SquareSize,
                  SquareSize, SquareSize);
          gridSwitcher = !gridSwitcher;
        }
        gridSwitcher = !gridSwitcher;
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".paintContents() error : " + e);
    }
  }

  private void paintText(String _Text, Graphics _G, Font _Font, Color _Color, int _X, int _Y)
  {
    try
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
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".paintText() error : " + e);
    }
  }

  public void processAction(String _Action, CEvent _Evt)
  {
    try
    {
      if (_Action.startsWith("Level"))
      {
        Level = (int) Integer.parseInt(_Action.substring(6));
        Task = "LoadLevel";
      }
      else if (_Action.startsWith("SaveLevel"))
      {
        Task = "SaveLevel";
      }
      else if (_Action.startsWith("ClearLevel"))
      {
        Contents = new int[H][W];
        paint();
      }
      else if (_Action.startsWith("MousePressed"))
      {
        java.awt.event.MouseEvent evt = (java.awt.event.MouseEvent) _Evt.getData();
        mousePressed(evt.getX(), evt.getY(), evt.isMetaDown());
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
        BgImage = Runner.createImage(Runner.getSize().width, Runner.getSize().height);
      }

      while (IsAlive)
      {
        if (Task == null)
        {
          sleep(50);
          continue;
        }

        Log.log("Task=" + Task);
        if (Task.equals("LoadLevel")) loadLevel();
        else if (Task.equals("SaveLevel")) saveLevel();

        paint();
        Task = null;
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".run() error : " + e);
    }
    Log.log(getClass().getName() + " is stopped.");
  }

  private void saveLevel()
  {
    try
    {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Level + ".lvl", false));
      oos.writeObject(Contents);
      oos.flush();
      oos.close();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".saveLevel() error : " + e);
    }
  }

  public void setStop()
  {
    IsAlive = false;
  }
}
