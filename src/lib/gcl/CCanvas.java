package lib.gcl;

import lib.util.Log;

import java.awt.*;

public class CCanvas extends Canvas implements Runnable
{
  protected Container Owner = null;
  protected String Text = "";
  protected CTip Tip = null;
  protected Event ME = null;      // Mouse Event
  protected Thread T = null;
  protected boolean IsAlive = true;
  protected java.awt.event.ActionListener AL = null;

  public CCanvas(Container _Owner)
  {
    super();

    Owner = _Owner;
    setBackground(Owner.getBackground());
    Tip = new CTip(this);
    T = new Thread(this);
    T.setPriority(Thread.MIN_PRIORITY);
    T.start();
  }

  public void finalize()
  {
    IsAlive = false;
    try
    {
      T.resume();
    }
    catch (Exception e)
    {
    }
  }

  public String getText()
  {
    return Text;
  }

  private void hideTip()
  {
    try
    {
      Tip.setVisible(false);
      if (Tip.getParent() != null) Tip.getParent().remove(Tip);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".hideTip() error : " + e);
    }
  }

  private void layoutTip()
  {
    Graphics g = null;
    FontMetrics fm = null;
    int w = 0, h = 0, x = 0, y = 0, dx = 0, dy = 0, n = 0, d = 10, mx = 0, my = 0, px = 0, py = 0;
    Container parent = null;

    try
    {
      if (getParent() == null || !Tip.isVisible()) return;


      parent = getParent();
      while (parent.getParent() != null && parent.getParent() instanceof Panel)
      {
        px += parent.getLocation().x;
        py += parent.getLocation().y;
        parent = parent.getParent();
      }

      g = getGraphics();
      if (g == null) g = parent.getGraphics();
      if (g == null) return;

      Tip.setSize(1, 1);
      Tip.setLocation(-999, -999);
      parent.add(Tip, 0);

      g.setFont(new Font("Dialog", 0, 12));
      fm = g.getFontMetrics();
      w = fm.stringWidth(Tip.Text);
      w = 8 + w;
      h = 16;
      Tip.setSize(w, h);

      if (ME != null)
      {
        mx = ME.x;
        my = ME.y;
      }
      x = px + getLocation().x + mx + d;
      y = py + getLocation().y + my + d;

      dx = x + Tip.getSize().width - parent.getSize().width;
      if (dx > 0)
      {
        if (dx < d) x -= dx;
        else x -= 2 * d + Tip.getSize().width;
      }
      dy = y + Tip.getSize().height - parent.getSize().height;
      if (dy > 0)
      {
        if (dy < d) y -= dy;
        else y -= 2 * d + Tip.getSize().height;
      }

      Tip.setLocation(x, y);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".layoutTip() error : " + e);
    }
  }

  public boolean mouseClick(Event evt, int x, int y)
  {
    IGCLClickListener l = null;

    try
    {
      if (!evt.metaDown())
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
      }
    }
    catch (Exception e1)
    {
    }

    return true;
  }

  public boolean mouseDown(Event evt, int x, int y)
  {
    ME = evt;

    paint();
    hideTip();

    return true;
  }

  public boolean mouseEnter(Event evt, int x, int y)
  {
    setCursor(new Cursor(Cursor.HAND_CURSOR));

    showTip(x, y);

    return true;
  }

  public boolean mouseExit(Event evt, int x, int y)
  {
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    paint();
    hideTip();

    return true;
  }

  public boolean mouseMove(Event evt, int x, int y)
  {
    ME = evt;
    return true;
  }

  public boolean mouseUp(Event evt, int x, int y)
  {
    if (ME != null)
      if (Math.abs(ME.x - x) < 5 && Math.abs(ME.y - y) < 5) mouseClick(evt, x, y);

    paint();

    return true;
  }

  public void paint()
  {
    paint(getGraphics());
  }

  public void paint(Graphics g)
  {
    g.setClip(0, 0, getSize().width, getSize().height);
    g.setColor(getBackground());
    super.paint(g);
  }

  public void run()
  {
    try
    {
      T.suspend();
      while (IsAlive)
      {
        T.sleep(Tip.Delay);
        layoutTip();
        T.suspend();
      }
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".run() error : " + e);
    }
  }

  public void setActionListener(java.awt.event.ActionListener _AL)
  {
    AL = _AL;
  }

  public void setEnabled(boolean _F)
  {
    if (isEnabled() != _F)
    {
      super.setEnabled(_F);
      paint();
    }
  }

  public void setShowTip(boolean _F)
  {
    Tip.setEnabled(_F);
  }

  public void setText(String _Text)
  {
    Text = _Text;
    paint();
  }

  public void setTip(String _Text)
  {
    Tip.Text = _Text;
  }

  public void setTipDelay(int _Delay)
  {
    Tip.Delay = _Delay;
  }

  private void showTip(int _X, int _Y)
  {
    try
    {
      if (!Tip.isEnabled()) return;
      if (Tip.Text == null || Tip.Text.length() < 1) return;
      Tip.setVisible(true);
      T.resume();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".showTip() error : " + e);
    }
  }
}
