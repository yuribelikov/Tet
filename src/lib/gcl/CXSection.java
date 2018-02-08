package lib.gcl;

import java.awt.*;

public class CXSection extends Panel
{
  private CXSectionsPanel Owner = null;
  public int N = 0, H = 100;

  public CXSection(CXSectionsPanel _Owner)
  {
    Owner = _Owner;
  }

  public void paint0(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0;

    try
    {
      w = getSize().width;
      h = getSize().height;
      g.setColor(Color.blue);

      g.drawRect(0, 0, w - 1, h - 1);

    }
    catch (Exception e1)
    {
    }

  }
}