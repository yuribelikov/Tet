package lib.gcl;

import java.awt.*;

public class CTab extends CTabCanvas
{
  public CTab(CTabsPanel _Owner, String _Caption, String _Tip)
  {
    super(_Owner);

    Text = _Caption;
    Tip.Text = _Tip;
  }

  public void paint(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0;

    try
    {
      w = getSize().width;
      h = getSize().height;
      super.paint(g);

      y = h / 2 + g.getFontMetrics(getFont()).getHeight() / 3;
      if (Selected)
      {
        g.setColor(getBackground());
        g.setClip(0, 0, w, h);
        g.fill3DRect(0, 0, w, h + 2, true);
        x = 4;
        y++;
        g.setColor(getForeground());
      }
      else
      {
        g.setColor(Color.gray);
        g.setClip(0, 0, w, h);
        g.drawRect(1, 1, w - 2, h + 2);
        g.setClip(2, 2, w - 3, h - 3);
        x = 5;
      }

      g.setClip(3, 1, w - 6, h - 2);
      g.drawString(Text, x, y);

    }
    catch (Exception e1)
    {
    }

  }
}