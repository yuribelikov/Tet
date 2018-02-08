package bel.tetris.dialog;

import bel.tetris.container.ImageContainer;
import lib.gcl.CGButton;
import lib.gcl.CInfoArea;
import lib.util.Log;

import java.awt.*;
import java.awt.event.ActionListener;

public class CYesNoBox extends CBox
{
  private CInfoArea InfoArea = null;

  public CYesNoBox()
  {
    super();

    create();
  }

  protected void create()
  {
    try
    {
      W = 300;
      H = 140;
      super.create();

      InfoArea = new CInfoArea(this);
      add(InfoArea);
      InfoArea.setWordWrap(true);
      InfoArea.setBounds(10, 30, W - 20, H - 75);
      InfoArea.setFont(new Font("Dialog", 0, 14));
      InfoArea.setAlign(CInfoArea.CENTER);

      OkButton = new CGButton(this, ImageContainer.getImage("okButton.gif"), "��");
      add(OkButton);
      OkButton.setName("OkButton");
      OkButton.setActionListener((ActionListener) EventListener);
      OkButton.setBounds(W - 75, H - 40, 30, 30);
      OkButton.setBorder(false);
      OkButton.setPopUpMode(true);
      CancelButton = new CGButton(this, ImageContainer.getImage("cancelButton.gif"), "���");
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

  public boolean showBox(String _Title, String _Text)
  {
    setTitle(_Title);

    Result = false;
    InfoArea.setText(_Text);
    OkButton.setState(1);
    CancelButton.setState(1);

    showSelf();

    return Result;
  }
}
