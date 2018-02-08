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

public class Runner extends Frame implements IEventReceiver
{
  private CCup[] Cups = null;
  private int PlayersQ = 1, OverQ = 0;
  String ScoresFileName = "scores.obj", OptionsFileName = "options.obj";
  private COptions Options = null;

  private CEventListener EventListener = null;
  private MenuBar MB = null;
  private Menu GameMenu = null;
  private MenuItem NewGameMI = null, ScoresMI = null, OptionsMI = null, QuitMI = null;

  public CYesNoBox YesNoBox = null;
  public CNewGameBox NewGameBox = null;
  public CScoresBox ScoresBox = null;
  public COptionsBox OptionsBox = null;

  public void destroy()
  {
    try
    {
      Cups[0].setStop();
      Cups[1].setStop();
      dispose();
//		System.runFinalization(); System.gc();
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.sop(getClass().getName() + ".destroy() error : " + e);
    }
  }

  public void dispatchEvent(CEvent _Evt)
  {
    try
    {
// - - - - - - - - key events - - - - - - - - -
      if (_Evt.getName().equals("KeyPressed"))
      {
        KeyEvent keyEvt = (KeyEvent) _Evt.getData();
        String keyKode = keyEvt.getKeyText(keyEvt.getKeyCode());

        for (int n = 0; n < Options.Keys.length; n++)
          for (int k = 0; k < Options.Keys[0].length; k++)
          {
            if (Options.Keys[n][k].equals(keyKode))
              Cups[n].processAction(Options.Actions[k]);
          }

        if (keyKode.equals("Pause"))
        {
          Cups[0].processAction("Pause");
          Cups[1].processAction("Pause");
        }
      }


// - - - - - - - - menu and window actions - - - - - - - 
      if (_Evt.getName().equals("ActionPerformed") &&
              _Evt.getSourceName().equals("NewGame"))
      {
        Cups[0].processAction("NewGame");
      }

      if (_Evt.getName().equals("ActionPerformed") &&
              _Evt.getSourceName().equals("Scores"))
      {
        ScoresBox.showBox();
      }

      if (_Evt.getName().equals("ActionPerformed") &&
              _Evt.getSourceName().equals("Options"))
      {
        COptions o = OptionsBox.showBox(Options);
        if (o != null)
        {
          Options = o;
          saveOptions();
        }
      }

      if ((_Evt.getName().equals("ActionPerformed") &&
              _Evt.getSourceName().equals("Quit")) ||
              (_Evt.getName().equals("WindowClosing")))
      {
        Cups[0].processAction("Quit");
        Cups[1].processAction("Quit");
        if (YesNoBox.showBox("�����������", "�� �������, ��� ������ ����� �� ��������� ?"))
        {
          Cups[0].finishGame();
          Cups[1].finishGame();
          destroy();
        }
        else
        {
          Cups[0].processAction("No");
          Cups[1].processAction("No");
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
    return PlayersQ;
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
      EventListener = new CEventListener(this);

      addKeyListener((KeyListener) EventListener);
      addWindowListener((WindowListener) EventListener);
      addComponentListener((ComponentListener) EventListener);

      Font f = new Font("Dialog", 0, 12);
      MB = new MenuBar();
      setMenuBar(MB);
      GameMenu = new Menu("����");
      MB.add(GameMenu);
      GameMenu.setFont(f);

      NewGameMI = new MenuItem("����� ����");
      NewGameMI.setName("NewGame");
      GameMenu.add(NewGameMI);
      NewGameMI.setFont(f);
      NewGameMI.addActionListener((ActionListener) EventListener);
      GameMenu.addSeparator();

      ScoresMI = new MenuItem("��� �����");
      ScoresMI.setName("Scores");
      GameMenu.add(ScoresMI);
      ScoresMI.setFont(f);
      ScoresMI.addActionListener((ActionListener) EventListener);
      GameMenu.addSeparator();

      OptionsMI = new MenuItem("���������");
      OptionsMI.setName("Options");
      GameMenu.add(OptionsMI);
      OptionsMI.setFont(f);
      OptionsMI.addActionListener((ActionListener) EventListener);
      GameMenu.addSeparator();

      QuitMI = new MenuItem("�����");
      QuitMI.setName("Quit");
      GameMenu.add(QuitMI);
      QuitMI.setFont(f);
      QuitMI.addActionListener((ActionListener) EventListener);

      YesNoBox = new CYesNoBox();
      NewGameBox = new CNewGameBox();
      ScoresBox = new CScoresBox(ScoresFileName);
      OptionsBox = new COptionsBox();

      Options = new COptions();
      loadOptions();
      Cups = new CCup[2];
      Cups[0] = new CCup(this, 1);
      Cups[1] = new CCup(this, 2);
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
      File f = new File(OptionsFileName);
      if (!f.exists()) return;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
      Options = (COptions) ois.readObject();
      ois.close();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".loadOptions() error : " + e);
    }
  }

  public static void main(String[] args)
  {
    Runner Application = null;

    try
    {
      Application = new Runner();
      Application.init(args);
      Application.start();
    }
    catch (Exception e)
    {
      Application.destroy();
    }
  }

  public void newGame()
  {
    try
    {
      java.util.Hashtable data = NewGameBox.showBox();
      if (data == null) return;

      Cups[0].finishGame();
      Cups[1].finishGame();
      getGraphics().clearRect(0, 0, getSize().width, getSize().height);
      OverQ = 0;
      PlayersQ = (int) Integer.parseInt((String) data.get("PlayersQ"));
      Cups[0].newGame(data);
      if (PlayersQ > 1) Cups[1].newGame(data);
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
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OptionsFileName, false));
      oos.writeObject(Options);
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

      Cups[0].start();
      Cups[1].start();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".start() error : " + e);
      destroy();
    }
  }

  public void updateScores(String _PlayerName, int _Score, boolean _IsShowScoreBox)
  {
    try
    {
      CScoreTable scoreTable = null;
      File f = new File(ScoresFileName);
      if (f.exists())
      {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        scoreTable = (CScoreTable) ois.readObject();
        ois.close();
      }
      else scoreTable = new CScoreTable();

      scoreTable.newRecord(_PlayerName, _Score);

      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ScoresFileName, false));
      oos.writeObject(scoreTable);
      oos.flush();
      oos.close();

      OverQ++;
      if (_IsShowScoreBox && OverQ == PlayersQ) ScoresBox.showBox();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".updateScores() error : " + e);
    }
  }
}
