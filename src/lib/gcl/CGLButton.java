package lib.gcl;

import java.awt.*;

public class CGLButton extends CButtonCanvas
{
 private Image Sprite=null;
 private int D=8;
public CGLButton(Container _Owner)
{
	this(_Owner, null, null, null);
}
public CGLButton(Container _Owner, Image _Sprite)
{
	this(_Owner, _Sprite, null, null);
}
public CGLButton(Container _Owner, Image _Sprite, String _Text)
{
	this(_Owner, _Sprite, _Text, null);
}
public CGLButton(Container _Owner, Image _Sprite, String _Text, String _Tip)
{
 super(_Owner);
 
	Sprite=_Sprite;
	Text=_Text;
	Tip.Text=_Tip;
}
public void autoWidth()
{
 int w=0;
 
	try
	{
		if (Text.length()>0) w=getFontMetrics(getFont()).stringWidth(Text)+2*D;
		else w=D;
		w+=Sprite.getWidth(Owner);
		setSize(w, getSize().height);
	}
	catch(Exception e){}
}
public void paint(Graphics g)
{
 int w=0, h=0, x=0, y=0, iw=0, ih=0, sw=0, sd=0;
 
	try
	{
		w=getSize().width; h=getSize().height;
		super.paint(g);
		
		iw=Sprite.getWidth(Owner); ih=Sprite.getHeight(Owner);
		g.setColor(getBackground());
		switch (State)
		{
			case UP : g.fill3DRect(1, 1, w-2, h-2, true); break;
			case DOWN :
				if (PopUpMode) g.fillRect(1, 1, w-2, h-2);
				else
				{
					g.fill3DRect(1, 1, w-2, h-2, false);
					g.fillRect(2, 2, w-4, h-4);
				}
				sd=1; break;
		}
		
		g.setClip(2, 2, w-4, h-4);
		y=h/2-ih/2+sd;
		g.drawImage(Sprite, 3+sd, y, this);
		g.setColor(getForeground());
		y=sd+h/2+g.getFontMetrics(getFont()).getHeight()/3;
		x=6+iw+sd;

		sw=g.getFontMetrics(getFont()).stringWidth(Text);
		switch (Alignment)
		{
			case ALIGN_LEFT : x=sd+iw+D; break;
			case ALIGN_CENTER : x=sd+iw+3+(w-iw-3)/2-sw/2; break;
			case ALIGN_RIGHT : x=sd+w-sw-D; break;
		}
		g.drawString(Text, x, y);

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
}