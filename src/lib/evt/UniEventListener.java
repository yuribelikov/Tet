package lib.evt;

import java.awt.event.*;

public class UniEventListener implements WindowListener, ComponentListener, KeyListener, MouseListener, MouseMotionListener, ActionListener, ItemListener, AdjustmentListener
{
  private UniEventReceiver EventReceiver;

  public UniEventListener(UniEventReceiver _EventReceiver)
  {
    EventReceiver = _EventReceiver;
  }

  public void actionPerformed(ActionEvent evt)
  {
    dispatchEvent(new UniEvent("ActionPerformed", evt));
  }

  public void adjustmentValueChanged(AdjustmentEvent evt)
  {
    dispatchEvent(new UniEvent("adjustmentValueChanged", evt));
  }

  public void componentHidden(ComponentEvent evt)
  {
    dispatchEvent(new UniEvent("ComponentHidden", evt));
  }

  public void componentMoved(ComponentEvent evt)
  {
    dispatchEvent(new UniEvent("ComponentMoved", evt));
  }

  public void componentResized(ComponentEvent evt)
  {
    dispatchEvent(new UniEvent("ComponentResized", evt));
  }

  public void componentShown(ComponentEvent evt)
  {
    dispatchEvent(new UniEvent("ComponentShown", evt));
  }

  public void dispatchEvent(UniEvent _Evt)
  {
//Log.log(_Evt);
    EventReceiver.dispatchEvent(_Evt);
  }

  public void itemStateChanged(ItemEvent evt)
  {
    dispatchEvent(new UniEvent("ItemStateChanged", evt));
  }

  public void keyPressed(KeyEvent evt)
  {
    dispatchEvent(new UniEvent("KeyPressed", evt));
  }

  public void keyReleased(KeyEvent evt)
  {
    dispatchEvent(new UniEvent("KeyReleased", evt));
  }

  public void keyTyped(KeyEvent evt)
  {
    dispatchEvent(new UniEvent("KeyTyped", evt));
  }

  public void mouseClicked(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseClicked", evt));
  }

  public void mouseDragged(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseDragged", evt));
  }

  public void mouseEntered(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseEntered", evt));
  }

  public void mouseExited(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseExited", evt));
  }

  public void mouseMoved(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseMoved", evt));
  }

  public void mousePressed(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MousePressed", evt));
  }

  public void mouseReleased(MouseEvent evt)
  {
    dispatchEvent(new UniEvent("MouseReleased", evt));
  }

  public void windowActivated(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowActivated", evt));
  }

  public void windowClosed(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowClosed", evt));
  }

  public void windowClosing(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowClosing", evt));
  }

  public void windowDeactivated(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowDeactivated", evt));
  }

  public void windowDeiconified(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowDeiconified", evt));
  }

  public void windowIconified(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowIconified", evt));
  }

  public void windowOpened(WindowEvent evt)
  {
    dispatchEvent(new UniEvent("WindowOpened", evt));
  }
}
