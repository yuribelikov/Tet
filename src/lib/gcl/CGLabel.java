package lib.gcl;

import java.awt.*;

public class CGLabel extends CLabelCanvas
{
  private Image Sprite = null;
  private boolean ImageToWidth = false;

  public CGLabel(Container _Owner)
  {
    this(_Owner, null, null);
  }

  public CGLabel(Container _Owner, Image _Sprite)
  {
    this(_Owner, _Sprite, null);
  }

  public CGLabel(Container _Owner, Image _Sprite, String _Tip)
  {
    super(_Owner);

    Sprite = _Sprite;
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

      if (ImageToWidth) g.drawImage(Sprite, 0, 0, w, h, this);
      else
      {
        sw = Sprite.getWidth(Owner);
        x = Alignment * (w - sw) / 2;
        y = h / 2 - Sprite.getHeight(Owner) / 2;
        g.drawImage(Sprite, x, y, this);
      }


      if (!isEnabled())
      {
        g.setColor(getBackground()); // new Color(210, 210, 210)
        for (x = 1; x < w - 2; x += 2)
          for (y = 1; y < h - 2; y += 2)
            g.drawLine(x, y, x, y);
      }

    }
    catch (Exception e1)
    {
    }

  }

  public void setExtendGraphicMode(boolean _F)
  {
    if (ImageToWidth == _F) return;

    ImageToWidth = _F;
    paint();
  }

  public void setImage(Image _im)
  {
    Sprite = _im;

    paint(getGraphics());
  }
}