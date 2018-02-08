package lib.gcl;

import java.awt.*;

public class CButtonCanvas extends CCanvas
{
  public final static int UP = 0;
  public final static int DOWN = 1;

  public static final int ALIGN_LEFT = 0;
  public static final int ALIGN_CENTER = 1;
  public static final int ALIGN_RIGHT = 2;

  protected int Alignment = ALIGN_CENTER;
  protected int State = 0;
  protected boolean PopUpMode = false;
  protected boolean Border = true;
  protected boolean RadioMode = false;

  public CButtonCanvas(Container _Owner)
  {
    super(_Owner);
  }

  public int getState()
  {
    return State;
  }

  public boolean mouseDown(Event evt, int x, int y)
  {
    IGCLClickListener l = null;

//	State=DOWN;

    super.mouseDown(evt, x, y);

    try
    {
      if (!evt.metaDown() && RadioMode)
      {
        if (AL != null)
        {
          AL.actionPerformed(new java.awt.event.ActionEvent(evt.target, evt.id, "GcClicked"));
        }
        else
        {
          l = (IGCLClickListener) Owner;
          l.gcClicked(evt);
        }

        if (RadioMode && State == DOWN)
        {
          setState(UP);
          return true;
        }
      }
    }
    catch (Exception e1)
    {
    }

    setState(DOWN);

    return true;
  }

  public boolean mouseEnter(Event evt, int x, int y)
  {
    if (PopUpMode)
    {
      State = UP;
      paint();
    }

    super.mouseEnter(evt, x, y);

    return true;
  }

  public boolean mouseExit(Event evt, int x, int y)
  {
    if (!RadioMode) State = UP;
    if (PopUpMode) State = DOWN;

    super.mouseExit(evt, x, y);

    return true;
  }

  public boolean mouseUp(Event evt, int x, int y)
  {
    if (!RadioMode && !PopUpMode) State = UP;
    if (PopUpMode && contains(x, y)) State = UP;

    if (!RadioMode) super.mouseUp(evt, x, y);

    return true;
  }

  public void paint(Graphics g)
  {
    try
    {
      super.paint(g);

      if (isEnabled() && Border && (!PopUpMode || State == UP)) g.setColor(Color.black);
      else g.setColor(getBackground());
      g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

    }
    catch (Exception e1)
    {
    }

  }

  public void setAlignment(int _Alignment)
  {
    if (Alignment == _Alignment) return;

    Alignment = _Alignment;
    paint();
  }

  public void setBorder(boolean _F)
  {
    if (Border == _F) return;

    Border = _F;
    paint();

  }

  public void setPopUpMode(boolean _F)
  {
    if (PopUpMode == _F) return;

    PopUpMode = _F;
    State = DOWN;
    paint();

  }

  public void setRadioMode(boolean _F)
  {
    RadioMode = _F;
  }

  public void setState(int _S)
  {
    if (State == _S) return;

    State = _S;
    paint();
  }
}