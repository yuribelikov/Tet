package bel.tetris.event;

import java.awt.event.*;

public class CEventListener implements WindowListener, ComponentListener, KeyListener, MouseListener, ActionListener, ItemListener
{
  private IEventReceiver eventReceiver = null;

  public CEventListener(IEventReceiver _EventReceiver)
  {
    eventReceiver = _EventReceiver;
  }

  public void actionPerformed(ActionEvent evt)
  {
    dispatchEvent(new CEvent("ActionPerformed", evt));
  }

  public void componentHidden(ComponentEvent evt)
  {
    dispatchEvent(new CEvent("ComponentHidden", evt));
  }

  public void componentMoved(ComponentEvent evt)
  {
    dispatchEvent(new CEvent("ComponentMoved", evt));
  }

  public void componentResized(ComponentEvent evt)
  {
    dispatchEvent(new CEvent("ComponentResized", evt));
  }

  public void componentShown(ComponentEvent evt)
  {
    dispatchEvent(new CEvent("ComponentShown", evt));
  }

  private void dispatchEvent(CEvent _Evt)
  {
    eventReceiver.dispatchEvent(_Evt);
  }

  public void itemStateChanged(ItemEvent evt)
  {
    dispatchEvent(new CEvent("ItemStateChanged", evt));
  }

  public void keyPressed(KeyEvent evt)
  {
    dispatchEvent(new CEvent("KeyPressed", evt));
  }

  public void keyReleased(KeyEvent evt)
  {
    dispatchEvent(new CEvent("KeyReleased", evt));
  }

  public void keyTyped(KeyEvent evt)
  {
    dispatchEvent(new CEvent("KeyTyped", evt));
  }

  public void mouseClicked(MouseEvent evt)
  {
    dispatchEvent(new CEvent("MouseClicked", evt));
  }

  public void mouseEntered(MouseEvent evt)
  {
    dispatchEvent(new CEvent("MouseEntered", evt));
  }

  public void mouseExited(MouseEvent evt)
  {
    dispatchEvent(new CEvent("MouseExited", evt));
  }

  public void mousePressed(MouseEvent evt)
  {
    dispatchEvent(new CEvent("MousePressed", evt));
  }

  public void mouseReleased(MouseEvent evt)
  {
    dispatchEvent(new CEvent("MouseReleased", evt));
  }

  public void windowActivated(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowActivated", evt));
  }

  public void windowClosed(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowClosed", evt));
  }

  public void windowClosing(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowClosing", evt));
  }

  public void windowDeactivated(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowDeactivated", evt));
  }

  public void windowDeiconified(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowDeiconified", evt));
  }

  public void windowIconified(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowIconified", evt));
  }

  public void windowOpened(WindowEvent evt)
  {
    dispatchEvent(new CEvent("WindowOpened", evt));
  }
}
