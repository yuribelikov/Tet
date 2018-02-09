package bel.tetris.game;

import bel.tetris.event.CEvent;
import bel.tetris.event.CEventListener;
import bel.tetris.event.IEventReceiver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Properties;

public class Tetris extends Frame implements IEventReceiver
{
  private CCup cup = null;


  public static void main(String[] args)
  {
    Tetris app = new Tetris();

    try
    {
      app.init();
      app.showSelf();
      app.cup.start();
    }
    catch (Exception e)
    {
      app.destroy();
    }
  }

  private void init()
  {
    try
    {
      setBackground(Color.black);
      setTitle("Tetris");
      setLayout(null);
      setResizable(false);
      CEventListener eventListener = new CEventListener(this);

      addKeyListener(eventListener);
      addWindowListener(eventListener);
      addComponentListener(eventListener);

      Font f = new Font("Dialog", 0, 12);
      MenuBar MB = new MenuBar();
      setMenuBar(MB);
      Menu gameMenu = new Menu("Game");
      MB.add(gameMenu);
      gameMenu.setFont(f);

      MenuItem newGameMI = new MenuItem("New Game");
      newGameMI.setName("NewGame");
      gameMenu.add(newGameMI);
      newGameMI.setFont(f);
      newGameMI.addActionListener(eventListener);
      gameMenu.addSeparator();

      cup = new CCup(this);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void newGame()
  {
    try
    {
      Properties data = new Properties();
      data.put("PlayersQ", "1");
      data.put("Player1Name", "PlayerName");
      data.put("Speed", "2");
      data.put("IsNextFigureShowed", "true");

      cup.finishGame();
      getGraphics().clearRect(0, 0, getSize().width, getSize().height);
      cup.newGame(data);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void dispatchEvent(CEvent evt)
  {
    String[] actions = {"MoveLeft", "MoveRight", "Rotate", "Drop"};
    String[] keys = {"Left", "Right", "Up", "Down"};

    try
    {
// - - - - - - - - key events - - - - - - - - -
      if (evt.getName().equals("KeyPressed"))
      {
        KeyEvent keyEvt = (KeyEvent) evt.getData();
        String keyCode = KeyEvent.getKeyText(keyEvt.getKeyCode());

        for (int n = 0; n < keys.length; n++)
          if (keys[n].equals(keyCode))
            cup.processAction(actions[n]);

        if (keyCode.equals("Pause"))
          cup.processAction("Pause");
      }

// - - - - - - - - menu and window actions - - - - - - -
      if (evt.getName().equals("ActionPerformed") && evt.getSourceName().equals("NewGame"))
        newGame();

      if (evt.getName().equals("WindowClosing"))
      {
        cup.processAction("Quit");
        cup.finishGame();
        destroy();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void showSelf()
  {
    Dimension d = getToolkit().getScreenSize();
    int w = 800;
    int h = 540;
    int x = d.width / 2 - w / 2;
    int y = d.height / 2 - h / 2;// y=0;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    setBounds(x, y, w, h);
    setVisible(true);
  }

  public void updateScores(String playerName, int score, boolean isShowScoreBox)
  {

  }

  private void destroy()
  {
    try
    {
      cup.setStop();
      dispose();
      System.exit(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
