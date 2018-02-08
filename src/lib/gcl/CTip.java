package lib.gcl;

import java.awt.*;
import lib.util.*;

public class CTip extends Canvas
{
 protected CCanvas Owner=null;
 protected String Text="";
 protected int Delay=600;
public CTip(CCanvas _Owner)
{
	super();

	Owner=_Owner;
	setBackground(SystemColor.info);
	setForeground(SystemColor.infoText);
}
public void paint(Graphics g)
{
	if (Text==null) return;

	g.drawRect(0, 0, getSize().width-1, getSize().height-1);
	g.drawString(Text, 4, getSize().height-3);
}
}