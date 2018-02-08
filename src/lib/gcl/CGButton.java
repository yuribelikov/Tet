package lib.gcl;

import java.awt.*;

public class CGButton extends CButtonCanvas
{
 private Image Sprite=null;
 private boolean ImageToWidth=false;
public CGButton(Container _Owner)
{
	this(_Owner, null, null);
}
public CGButton(Container _Owner, Image _Sprite)
{
	this(_Owner, _Sprite, null);
}
public CGButton(Container _Owner, Image _Sprite, String _Tip)
{
 super(_Owner);
 
	Sprite=_Sprite;
	Tip.Text=_Tip;
}
public void paint(Graphics g)
{
 int w=0, h=0, x=0, y=0; 
 
	try
	{
		w=getSize().width; h=getSize().height;
		super.paint(g);
		
		g.setColor(getBackground());
		if (State==UP)
		{
			g.fill3DRect(1, 1, w-2, h-2, true);
			g.setClip(2, 2, w-4, h-4);
			if (ImageToWidth)	g.drawImage(Sprite, 2, 2, w-4, h-4, this);
			else
			{
				x=w/2-Sprite.getWidth(Owner)/2;
				y=h/2-Sprite.getHeight(Owner)/2;
				g.drawImage(Sprite, x, y, this);
			}
		}
		
		if (State==DOWN)
		{
			if (PopUpMode) g.fillRect(1, 1, w-2, h-2);
			else
			{
				g.fill3DRect(1, 1, w-2, h-2, false);
				g.fillRect(2, 2, w-4, h-4);
			}
			g.setClip(2, 2, w-4, h-4);
			if (ImageToWidth)	g.drawImage(Sprite, 3, 3, w-4, h-4, this);
			else 
			{
				x=w/2-Sprite.getWidth(Owner)/2+1;
				y=h/2-Sprite.getHeight(Owner)/2+1;
				g.drawImage(Sprite, x, y, this);
			}
		}

		if (!isEnabled())
		{
			g.setColor(new Color(210, 210, 210));
			for (x=1; x<w-2; x+=2)
			for (y=1; y<h-2; y+=2)
				g.drawLine(x, y, x, y);
		}
	}
	catch(Exception e1){}

}
public void setExtendGraphicMode(boolean _F)
{
	if (ImageToWidth==_F) return;
	
	ImageToWidth=_F; paint();
}
public void setImage(Image _im)
{
	Sprite = _im;

	paint(getGraphics());
}
}