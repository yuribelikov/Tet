package lib.gcl;

import java.awt.*;

public class CButton extends CButtonCanvas
{
public CButton(Container _Owner)
{
	this(_Owner, null, null);
}
public CButton(Container _Owner, String _Text)
{
	this(_Owner, _Text, null);
}
public CButton(Container _Owner, String _Text, String _Tip)
{
 super(_Owner);
 
	Text=_Text;
	Tip.Text=_Tip;
}
public void paint(Graphics g)
{
 int w=0, h=0, x=0, y=0, sw=0, d=4;
 
	try
	{
		w=getSize().width; h=getSize().height;
		super.paint(g);

		sw=g.getFontMetrics(getFont()).stringWidth(Text);
		switch (Alignment)
		{
			case ALIGN_LEFT : x=d; break;
			case ALIGN_CENTER : x=(w-sw)/2; break;
			case ALIGN_RIGHT : x=w-sw-d; break;
		}
		y=h/2+g.getFontMetrics(getFont()).getHeight()/3;
		
		g.setColor(getBackground());
		if (State==UP)
		{
			g.fill3DRect(1, 1, w-2, h-2, true);
			g.setClip(2, 2, w-4, h-4);
			if (isEnabled()) g.setColor(getForeground());
			else g.setColor(Color.gray);
			g.drawString(Text, x, y);
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
			if (isEnabled()) g.setColor(getForeground());
			else g.setColor(Color.gray);
			g.drawString(Text, x+1, y+1);
		}
		
	}
	catch(Exception e1){}

}
}