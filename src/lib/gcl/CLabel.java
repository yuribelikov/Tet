package lib.gcl;

import java.awt.*;

public class CLabel extends CLabelCanvas
{
  public CLabel(Container _Owner)
  {
    this(_Owner, null, null);
  }

  public CLabel(Container _Owner, String _Text)
  {
    this(_Owner, _Text, null);
  }

  public CLabel(Container _Owner, String _Text, String _Tip)
  {
    super(_Owner);

    Text = _Text;
    Tip.Text = _Tip;
  }

  public void paint(Graphics g)
  {
    int w = 0, h = 0, x = 0, y = 0, sw = 0;

    try
    {
      w = getSize().width;
      h = getSize().height;
      super.paint(g);

      if (isEnabled()) g.setColor(getForeground());
      else g.setColor(Color.gray);
      sw = g.getFontMetrics(getFont()).stringWidth(Text);
      x = Alignment * (w - sw) / 2;
      g.drawString(Text, x, h / 2 + g.getFontMetrics(getFont()).getHeight() / 3);

    }
    catch (Exception e1)
    {
    }

  }
}