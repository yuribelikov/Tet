package bel.tetris.dialog;

import bel.tetris.container.ImageContainer;
import bel.tetris.event.CEvent;
import lib.gcl.CGButton;
import lib.util.Log;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Hashtable;

public class CNewGameBox extends CBox
{
  private Hashtable Data = null;

  private Choice PlayersQChoice = null, SpeedChoice = null;
  private Label[] PlayerNamesLabels = null;
  private TextField[] PlayerNames = null;
  private Checkbox ShowNextFigureCheckbox = null;

  public CNewGameBox()
  {
    super();

    create();
  }

  protected void create()
  {
    try
    {
      W = 400;
      H = 250;
      super.create();

      setTitle("����� ����");
      Data = new Hashtable();

      Label l = new Label("��� ���� :");
      add(l);
      l.setBounds(10, 35, 210, 20);
      PlayersQChoice = new Choice();
      add(PlayersQChoice);
      PlayersQChoice.setBounds(220, 35, W - 230, 20);
      PlayersQChoice.addItem("���� �����");
      PlayersQChoice.addItem("��� ������");
      PlayersQChoice.select(0);
      PlayersQChoice.addItemListener((ItemListener) EventListener);

      PlayerNamesLabels = new Label[2];
      PlayerNames = new TextField[2];
      PlayerNamesLabels[0] = new Label("��� ������ :");
      add(PlayerNamesLabels[0]);
      PlayerNamesLabels[0].setBounds(10, 65, 210, 20);
      PlayerNames[0] = new TextField("����� 1");
      add(PlayerNames[0]);
      PlayerNames[0].setBounds(220, 65, W - 230, 20);
      PlayerNamesLabels[1] = new Label("��� ������� ������ :");
      add(PlayerNamesLabels[1]);
      PlayerNamesLabels[1].setBounds(10, 95, 210, 20);
      PlayerNamesLabels[1].setVisible(false);
      PlayerNames[1] = new TextField("����� 2");
      add(PlayerNames[1]);
      PlayerNames[1].setBounds(220, 95, W - 230, 20);
      PlayerNames[1].setVisible(false);

      l = new Label("�������� :");
      add(l);
      l.setBounds(10, 125, 210, 20);
      SpeedChoice = new Choice();
      add(SpeedChoice);
      SpeedChoice.setBounds(220, 125, W - 230, 20);
      SpeedChoice.addItem("����������");
      SpeedChoice.addItem("���������");
      SpeedChoice.addItem("����������");
      SpeedChoice.addItem("�������");
      SpeedChoice.addItem("����������");
      SpeedChoice.select(2);

      l = new Label("����� ��������� ������ :");
      add(l);
      l.setBounds(10, 155, 210, 20);
      ShowNextFigureCheckbox = new Checkbox();
      add(ShowNextFigureCheckbox);
      ShowNextFigureCheckbox.setBounds(220, 156, W - 230, 18);
      ShowNextFigureCheckbox.setState(true);

      OkButton = new CGButton(this, ImageContainer.getImage("okButton.gif"), "������  ");
      add(OkButton);
      OkButton.setName("OkButton");
      OkButton.setActionListener((ActionListener) EventListener);
      OkButton.setBounds(W - 75, H - 40, 30, 30);
      OkButton.setBorder(false);
      OkButton.setPopUpMode(true);
      CancelButton = new CGButton(this, ImageContainer.getImage("cancelButton.gif"), "������  ");
      add(CancelButton);
      CancelButton.setName("CancelButton");
      CancelButton.setActionListener((ActionListener) EventListener);
      CancelButton.setBounds(W - 40, H - 40, 30, 30);
      CancelButton.setBorder(false);
      CancelButton.setPopUpMode(true);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".create() : " + e);
    }
  }

  public void dispatchEvent(CEvent _Evt)
  {
    try
    {
      super.dispatchEvent(_Evt);

      if (_Evt.getName().equals("ItemStateChanged") && _Evt.getSource() == PlayersQChoice)
      {
        if (PlayersQChoice.getSelectedIndex() == 0)
          PlayerNamesLabels[0].setText("��� ������ :");
        else
          PlayerNamesLabels[0].setText("��� ������� ������ :");
        PlayerNamesLabels[1].setVisible(PlayersQChoice.getSelectedIndex() == 1);
        PlayerNames[1].setVisible(PlayersQChoice.getSelectedIndex() == 1);
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".dispatchEvent() error : " + e);
    }
  }

  public Hashtable showBox()
  {
    Result = false;
    Data.clear();
    OkButton.setState(1);
    CancelButton.setState(1);

    showSelf();

    Data.put("PlayersQ", "" + (1 + PlayersQChoice.getSelectedIndex()));
    Data.put("Player1Name", PlayerNames[0].getText());
    Data.put("Player2Name", PlayerNames[1].getText());
    Data.put("Speed", "" + SpeedChoice.getSelectedIndex());
    Data.put("IsNextFigureShowed", "" + ShowNextFigureCheckbox.getState());

    if (Result) return Data;
    return null;
  }
}
