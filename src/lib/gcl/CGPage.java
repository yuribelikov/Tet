package lib.gcl;

import java.awt.*;

public class CGPage extends Panel
{
  private CGTabsPanel Owner = null;
  public int N = 0;

  public CGPage(CGTabsPanel _Owner)
  {
    Owner = _Owner;
  }

  public void paint(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0;

    try
    {
      w = getSize().width;
      h = getSize().height;
      g.setColor(getBackground());
      g.fill3DRect(0, 0, w, h, true);

      x = Owner.Tab[N].getLocation().x;
      w = Owner.Tab[N].getSize().width;
      g.fillRect(x + 1, 0, w - 2, 2);

    }
    catch (Exception e1)
    {
    }

  }
}