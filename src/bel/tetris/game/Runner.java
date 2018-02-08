package bel.tetris.game;

import bel.tetris.container.ImageContainer;
import bel.tetris.dialog.CNewGameBox;
import bel.tetris.dialog.COptionsBox;
import bel.tetris.dialog.CScoresBox;
import bel.tetris.dialog.CYesNoBox;
import bel.tetris.event.CEvent;
import bel.tetris.event.CEventListener;
import bel.tetris.event.IEventReceiver;
import lib.util.Log;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;

public class Runner extends Frame implements IEventReceiver
{
  private CCup[] cups = null;
  private int playersQ = 1, overQ = 0;
  String scoresFileName = "scores.obj", optionsFileName = "options.obj";
  private COptions options = null;

  private CEventListener eventListener = null;
  private MenuBar MB = null;
  private Menu gameMenu = null;
  private MenuItem newGameMI = null, scoresMI = null, optionsMI = null, quitMI = null;

  public CYesNoBox yesNoBox = null;
  public CNewGameBox newGameBox = null;
  public CScoresBox scoresBox = null;
  public COptionsBox optionsBox = null;


  public void destroy()
  {
    try
    {
      cups[0].setStop();
      cups[1].setStop();
      dispose();
//		System.runFinalization(); System.gc();
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.sop(getClass().getName() + ".destroy() error : " + e);
    }
  }

  public void dispatchEvent(CEvent evt)
  {
    try
    {
// - - - - - - - - key events - - - - - - - - -
      if (evt.getName().equals("KeyPressed"))
      {
        KeyEvent keyEvt = (KeyEvent) evt.getData();
        String keyKode = keyEvt.getKeyText(keyEvt.getKeyCode());

        for (int n = 0; n < options.Keys.length; n++)
          for (int k = 0; k < options.Keys[0].length; k++)
          {
            if (options.Keys[n][k].equals(keyKode))
              cups[n].processAction(options.Actions[k]);
          }

        if (keyKode.equals("Pause"))
        {
          cups[0].processAction("Pause");
          cups[1].processAction("Pause");
        }
      }


// - - - - - - - - menu and window actions - - - - - - - 
      if (evt.getName().equals("ActionPerformed") &&
              evt.getSourceName().equals("NewGame"))
      {
        cups[0].processAction("NewGame");
      }

      if (evt.getName().equals("ActionPerformed") &&
              evt.getSourceName().equals("scores"))
      {
        scoresBox.showBox();
      }

      if (evt.getName().equals("ActionPerformed") &&
              evt.getSourceName().equals("options"))
      {
        COptions o = optionsBox.showBox(options);
        if (o != null)
        {
          options = o;
          saveOptions();
        }
      }

      if ((evt.getName().equals("ActionPerformed") &&
              evt.getSourceName().equals("Quit")) ||
              (evt.getName().equals("WindowClosing")))
      {
        cups[0].processAction("Quit");
        cups[1].processAction("Quit");
        if (yesNoBox.showBox("�����������", "�� �������, ��� ������ ����� �� ��������� ?"))
        {
          cups[0].finishGame();
          cups[1].finishGame();
          destroy();
        }
        else
        {
          cups[0].processAction("No");
          cups[1].processAction("No");
        }
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".dispatchEvent() error : " + e);
    }
  }

  public int getPlayersQ()
  {
    return playersQ;
  }

  private void init(String _Args[])
  {
    try
    {
//Log.LogEnabled=true;
      setBackground(Color.black);
      setTitle("������");
      setLayout(null);
      setResizable(false);
      ImageContainer.load(this);
      setIconImage(ImageContainer.getImage("icon.gif"));
      eventListener = new CEventListener(this);

      addKeyListener((KeyListener) eventListener);
      addWindowListener((WindowListener) eventListener);
      addComponentListener((ComponentListener) eventListener);

      Font f = new Font("Dialog", 0, 12);
      MB = new MenuBar();
      setMenuBar(MB);
      gameMenu = new Menu("����");
      MB.add(gameMenu);
      gameMenu.setFont(f);

      newGameMI = new MenuItem("����� ����");
      newGameMI.setName("NewGame");
      gameMenu.add(newGameMI);
      newGameMI.setFont(f);
      newGameMI.addActionListener((ActionListener) eventListener);
      gameMenu.addSeparator();

      scoresMI = new MenuItem("��� �����");
      scoresMI.setName("scores");
      gameMenu.add(scoresMI);
      scoresMI.setFont(f);
      scoresMI.addActionListener((ActionListener) eventListener);
      gameMenu.addSeparator();

      optionsMI = new MenuItem("���������");
      optionsMI.setName("options");
      gameMenu.add(optionsMI);
      optionsMI.setFont(f);
      optionsMI.addActionListener((ActionListener) eventListener);
      gameMenu.addSeparator();

      quitMI = new MenuItem("�����");
      quitMI.setName("Quit");
      gameMenu.add(quitMI);
      quitMI.setFont(f);
      quitMI.addActionListener((ActionListener) eventListener);

      yesNoBox = new CYesNoBox();
      newGameBox = new CNewGameBox();
      scoresBox = new CScoresBox(scoresFileName);
      optionsBox = new COptionsBox();

      options = new COptions();
      loadOptions();
      cups = new CCup[2];
      cups[0] = new CCup(this, 1);
      cups[1] = new CCup(this, 2);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".init() error : " + e);
    }
  }

  private void loadOptions()
  {
    try
    {
      File f = new File(optionsFileName);
      if (!f.exists()) return;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      options = (COptions) ois.readObject();
      ois.close();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".loadOptions() error : " + e);
    }
  }

  public static void main(String[] args)
  {
    Runner app = null;

    try
    {
      app = new Runner();
      app.init(args);
      app.start();
    }
    catch (Exception e)
    {
      app.destroy();
    }
  }

  public void newGame()
  {
    try
    {
      Hashtable data = newGameBox.showBox();
      if (data == null) return;

      cups[0].finishGame();
      cups[1].finishGame();
      getGraphics().clearRect(0, 0, getSize().width, getSize().height);
      overQ = 0;
      playersQ = (int) Integer.parseInt((String) data.get("PlayersQ"));
      cups[0].newGame(data);
      if (playersQ > 1) cups[1].newGame(data);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".newGame() error : " + e);
    }
  }

  private void saveOptions()
  {
    try
    {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(optionsFileName, false));
      oos.writeObject(options);
      oos.flush();
      oos.close();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".saveOptions() error : " + e);
    }
  }

  private void showSelf()
  {
    Dimension d = getToolkit().getScreenSize();
    int x = 0, y = 0, w = 0, h = 0;

    w = 800;
    h = 540;
    x = d.width / 2 - w / 2;
    y = d.height / 2 - h / 2;// y=0;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    setBounds(x, y, w, h);
    show();
  }

  private void start()
  {
    try
    {
      showSelf();

      cups[0].start();
      cups[1].start();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".start() error : " + e);
      destroy();
    }
  }

  public void updateScores(String playerName, int score, boolean isShowScoreBox)
  {
    try
    {
      CScoreTable scoreTable = null;
      File f = new File(scoresFileName);
      if (f.exists())
      {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        scoreTable = (CScoreTable) ois.readObject();
        ois.close();
      }
      else scoreTable = new CScoreTable();

      scoreTable.newRecord(playerName, score);

      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scoresFileName, false));
      oos.writeObject(scoreTable);
      oos.flush();
      oos.close();

      overQ++;
      if (isShowScoreBox && overQ == playersQ) scoresBox.showBox();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
