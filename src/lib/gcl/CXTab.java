package lib.gcl;

import java.awt.*;

public class CXTab extends CCanvas
{
  public boolean Expanded = false, OnlyOne = false;
  private String Description = "";
  public int N = 0, DescriptionX = 200;

  public CXTab(Container _Owner, String _Caption)
  {
    super(_Owner);

    Text = _Caption;
  }

  public void paint(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0;
    String str = "";

    try
    {
      w = getSize().width;
      h = getSize().height;
      super.paint(g);

      g.setColor(getBackground());
      g.setClip(0, 0, w, h);

      if (isEnabled()) g.setColor(Color.blue);
      else g.setColor(Color.gray);
      if (Expanded)
      {
        x = 3;
        g.drawLine(x + 1, y + h / 2 - 3, x + 11, y + h / 2 - 3);
        g.drawLine(x + 2, y + h / 2 - 2, x + 10, y + h / 2 - 2);
        g.drawLine(x + 3, y + h / 2 - 1, x + 9, y + h / 2 - 1);
        g.drawLine(x + 4, y + h / 2, x + 8, y + h / 2);
        g.drawLine(x + 5, y + h / 2 + 1, x + 7, y + h / 2 + 1);
        g.drawLine(x + 6, y + h / 2 + 2, x + 6, y + h / 2 + 2);
        g.setColor(Color.black);
        g.drawLine(x + 6 + 1, y + h / 2 + 2, x + 11 + 1, y + h / 2 - 3);
      }
      else
      {
        x = 5;
        g.drawLine(x + 2, y + h / 2 - 5, x + 2, y + h / 2 + 4);
        g.drawLine(x + 3, y + h / 2 - 4, x + 3, y + h / 2 + 3);
        g.drawLine(x + 4, y + h / 2 - 3, x + 4, y + h / 2 + 2);
        g.drawLine(x + 5, y + h / 2 - 2, x + 5, y + h / 2 + 1);
        g.drawLine(x + 6, y + h / 2 - 1, x + 6, y + h / 2);
        g.setColor(Color.black);
        g.drawLine(x + 2, y + h / 2 + 4 + 1, x + 6, y + h / 2 + 1);
      }

      g.setClip(0, 0, w - 20, h);
      if (isEnabled()) g.setColor(Color.black);
      else g.setColor(Color.gray);
      y = h / 2 + g.getFontMetrics(getFont()).getHeight() / 3;
      g.drawString(Text, 18, y);

      if (Description.length() < 1) return;

      str = "[ " + Description + " ]";
      if (g.getFontMetrics(getFont()).stringWidth(str) > w - DescriptionX - 40)
      {
        g.drawString("... ]", w - 58, y);
        g.setClip(0, 0, w - 60, h);
      }
      g.drawString(str, DescriptionX, y);

    }
    catch (Exception e1)
    {
    }

  }

  public void setDescription(String _Str)
  {
    Description = _Str;
    paint();
  }
}