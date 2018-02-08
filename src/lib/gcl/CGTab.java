package lib.gcl;

import java.awt.*;

public class CGTab extends CTabCanvas
{
 private Image Sprite = null;
 private int Style = 0;
 // Style constants
 public static final int STYLE_STANDART = 0;
 public static final int STYLE_3D = 1;
public CGTab(CGTabsPanel _Owner, Image _Sprite, String _Tip)
{
 super(_Owner);
 
	Sprite=_Sprite;
	Tip.Text=_Tip;
}
public void paint(Graphics g)
{
	int w = 0, h = 0, x = 0, y = 0;
	try
	{
		w = getSize().width;
		h = getSize().height;
		super.paint(g);
		g.setClip(0, 0, w, h);
		if (Selected)
		{
			if (Style == CGTab.STYLE_STANDART)
			{
				g.setColor(getBackground());
				g.fill3DRect(0, 0, w, h + 2, true);
			}
			else
			{
				if (Style == CGTab.STYLE_3D)
				{
					g.setColor(Color.white);
					g.drawLine(0, 2, 0, h);
					g.drawLine(1, 1, 1, 1);
					g.drawLine(2, 0, w - 3, 0);
					g.setColor(Color.gray);
					g.drawLine(w - 2, 2, w - 2, h);
					g.setColor(Color.black);
					g.drawLine(w - 2, 1, w - 2, 1);
					g.drawLine(w - 1, 2, w - 1, h);
				} // if
			} // else
		}
		else
		{
			if (Style == CGTab.STYLE_STANDART)
			{
				g.setColor(Color.gray);
				g.drawRect(1, 1, w - 2, h + 2);
			}
			else
			{
				if (Style == CGTab.STYLE_3D)
				{
					g.setColor(Color.white);
					g.drawLine(0, 2, 0, h);
					g.drawLine(1, 1, 1, 1);
					g.drawLine(2, 0, w - 3, 0);
					g.setColor(Color.gray);
					g.drawLine(w - 2, 2, w - 2, h);
					g.setColor(Color.black);
					g.drawLine(w - 2, 1, w - 2, 1);
					g.drawLine(w - 1, 2, w - 1, h);
					g.setClip(2, 2, w - 3, h - 3);
				}
			}
		} // else

		g.setClip(2, 2, w-4, h-4);
//		g.setColor(Color.blue);
//		g.fillRect(x, y, w, h);
		g.drawImage(Sprite, (w-Sprite.getWidth(this))/2, (h-Sprite.getHeight(this))/2, this);
	}
	catch (Exception e1)
	{
	}
}
public void setImage(Image _Sprite)
{
	Sprite=_Sprite; paint();
}
public void setStyle(int _Style)
{
	Style = _Style;
}
}