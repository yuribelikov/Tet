package bel.tetris.game;

import java.awt.*;

class Painter extends Thread
{
  private final Tetris tetris;
  private final Game game;
  private final static int SQUARE_SIZE = 20;

  private final Image bgImage;

  private final static Color[] PALETTE = {null, new Color(222, 222, 222), Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
  private Color cupColor = new Color(150, 150, 255);
  private Color figureColor = new Color(180, 255, 180);
  private Color mergeColor = new Color(255, 150, 255);
  private Color scoreColor = new Color(220, 220, 255);
  private Font scoreFont = new Font("Dialog", 1, 24);
  private Color messageColor = new Color(255, 255, 150);
  private Font messageFont = new Font("Dialog", 1, 60);
  private Color InfoColor = new Color(220, 220, 240);
  private Font InfoFont = new Font("Dialog", 1, 48);

  Painter(Tetris tetris, Game game)
  {
    this.tetris = tetris;
    this.game = game;
    bgImage = tetris.createImage(tetris.getSize().width, tetris.getSize().height);
    start();
  }

  public void run()
  {
    System.out.println("Painter is started.");
    try
    {
      while (game.isAlive)
      {
        while (!game.repaint)
          sleep(1);

        game.repaint = false;
        paint();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Painter is stopped.");
  }

  private void paint()
  {
    Graphics g = bgImage.getGraphics();
    g.setColor(Color.black);
    g.fillRect(0, 0, tetris.getSize().width, tetris.getSize().height);

    g.setColor(cupColor);
    int w = SQUARE_SIZE * Cup.W;
    int h = SQUARE_SIZE * Cup.H;
    int x = tetris.getSize().width / 4 - w / 2;
    int y = tetris.getSize().height / 2 - h / 2 + 30;
    g.fillRect(x - 4, y, 4, h + 4);
    g.fillRect(x + w, y, 4, h + 4);
    g.fillRect(x, y + h, w, 4);

    paintContents(g, x, y);
    paintFigure(game.currentFigure, g, figureColor, x, y);
    paintFigure(game.nextFigure, g, figureColor, x + w, y + h / 2 - SQUARE_SIZE * 2);

    if (game.message != null)
      paintText(game.message, g, messageFont, messageColor, x + w / 2, y + 80);

    paintText("" + game.level, g, InfoFont, InfoColor, x + w + 50, y + 20);
    paintText(game.prize == 0 ? ("" + game.score) : (game.score + " + " + game.prize), g, scoreFont, scoreColor, x + w / 2, y + h);
    int mx = x + w / 2, my = y + h / 2 - 50;
    if (game.prize > 0)
      paintText("" + game.prize, g, messageFont, messageColor, mx, my);

    switch (game.state)
    {
      case Game.STATE_PAUSED:
        paintText("PAUSED", g, messageFont, messageColor, mx, my);
        break;
      case Game.STATE_GAME_OVER:
        paintText("GAME", g, messageFont, messageColor, mx, my);
        paintText("OVER", g, messageFont, messageColor, mx, my + 60);
        break;
      case Game.STATE_NO_MORE_LEVELS:
        paintText("GET MORE", g, messageFont, messageColor, mx, my);
        paintText("LEVELS", g, messageFont, messageColor, mx, my + 60);
        break;
    }

    tetris.getGraphics().drawImage(bgImage, tetris.getSize().width / 4, 0, tetris);
  }

  private void paintContents(Graphics g, int cx, int cy)
  {
    for (int y = 0; y < Cup.H; y++)
    {
      boolean isRowComplete = game.cup.isRowComplete(y);
      for (int x = 0; x < Cup.W; x++)
        if (game.cup.contents[y][x] > 0)
        {
          g.setColor(isRowComplete ? mergeColor : PALETTE[game.cup.contents[y][x]]);
          g.fillRect(cx + x * SQUARE_SIZE, cy + y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }
  }

  private void paintFigure(Figure figure, Graphics g, Color c, int fx, int fy)
  {
    if (figure == null)
      return;

    g.setColor(c);
    for (int y = 0; y < Figure.SIZE; y++)
      for (int x = 0; x < Figure.SIZE; x++)
        if (figure.getCurrContents()[y][x])
          g.fillRect(fx + (figure.pos.x + x) * SQUARE_SIZE, fy + (figure.pos.y + y) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
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

}
