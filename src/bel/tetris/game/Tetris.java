package bel.tetris.game;

import bel.tetris.event.CEvent;
import bel.tetris.event.CEventListener;
import bel.tetris.event.IEventReceiver;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Tetris extends Frame implements IEventReceiver
{
  private Game game = null;


  public static void main(String[] args)
  {
    Tetris app = new Tetris();

    try
    {
      app.init();
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
            game.move(actions[n]);

        if (keyCode.equals("Pause"))
          game.pause();
      }

// - - - - - - - - menu and window actions - - - - - - -
      if (evt.getName().equals("ActionPerformed") && evt.getSourceName().equals("NewGame"))
      {
        if (game != null)
          game.finishGame();

        game = new Game(this);
      }

      if (evt.getName().equals("WindowClosing"))
      {
        if (game != null)
        {
          game.move("Quit");
          game.finishGame();
        }
        destroy();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void destroy()
  {
    try
    {
      if (game != null)
        game.setStop();
      dispose();
      System.exit(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
