package bel.tetris.game;

class Game extends Thread
{
  final static int STATE_GAME = 1;
  final static int STATE_PAUSED = 2;
  final static int STATE_DROPPING = 3;
  final static int STATE_GAME_OVER = 4;
  final static int STATE_NO_MORE_LEVELS = 5;

  final Cup cup;
  private final Painter painter;
  int state = STATE_GAME;
  boolean repaint = false;
  Figure currentFigure = new Figure();
  Figure nextFigure = new Figure();
  private int speed = 1;
  int level = 1, score = 0, prize = 0;
  String message = null;
  boolean isAlive = true;


  Game(Tetris tetris)
  {
    super();

    cup = new Cup(level);
    painter = new Painter(tetris, this);
    start();
  }

  public void run()
  {
    System.out.println("Game is started.");
    try
    {
      while (isAlive)
      {
        repaint = true;
        if (state == STATE_PAUSED)
        {
          sleepMs(50);
          continue;
        }

        if (state == STATE_DROPPING)
          sleepMs(10);
        else
        {
          int delay = 1000 - speed * 450;
          while (delay-- > 0 && state == STATE_GAME && isAlive)    // dropping started or paused
            sleep(1);
        }

        currentFigure.pos.y++;
        if (cup.isFigurePositionValid(currentFigure))      // end of normal loop
          continue;

        currentFigure.pos.y--;    // figure has reached a bottom shape
        int mergedRows = cup.canMerge(currentFigure);
        if (mergedRows > 0)
        {
          prize = 100 * mergedRows * mergedRows * level * (speed + 1);
          repaint = true;
          sleepMs(500);
          score += prize;
          prize = 0;
          cup.merge();
          repaint = true;
        }

        if (cup.isLevelComplete())
          nextLevel();

        nextFigure();
        if (!cup.isFigurePositionValid(currentFigure))
        {
          state = STATE_GAME_OVER;
          repaint = true;
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
    System.out.println("Game is stopped.");
  }

  private void nextFigure()
  {
    currentFigure = nextFigure;
    nextFigure = new Figure();
  }

  void finishGame()
  {
//    tetris.updateScores("", score, false);
    isAlive = false;
  }

  void pause()
  {
    if (state == STATE_GAME)
      state = Game.STATE_PAUSED;
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
        if (!cup.isFigurePositionValid(currentFigure))
          currentFigure.pos.x++;
        break;
      case "MoveRight":
        currentFigure.pos.x++;
        if (!cup.isFigurePositionValid(currentFigure))
          currentFigure.pos.x--;
        break;
      case "Rotate":
        currentFigure.rotate();
        if (!cup.isFigurePositionValid(currentFigure))
          currentFigure.rotateBack();
        break;
      case "Drop":
        state = STATE_DROPPING;
        break;
    }

    repaint = true;
  }

  private void nextLevel() throws Exception
  {
    currentFigure = null;
    repaint = true;
    sleepMs(500);
    prize = 5000 * level * (speed + 1);
    message = "Bonus";
    repaint = true;
    sleepMs(1000);    // TODO: increase to 3000
    score += prize;
    prize = 0;
    message = null;
    repaint = true;
    sleepMs(500);
    level++;
    if (cup.loadLevel(level))
      message = "Next Level: " + level;
    else
    {
      state = STATE_NO_MORE_LEVELS;
//              tetris.updateScores("", score, true);
    }
    repaint = true;
    sleepMs(2000);
    message = null;
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

}
