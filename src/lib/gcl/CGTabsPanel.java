package lib.gcl;

import java.awt.*;

public class CGTabsPanel extends CTabsPanelCanvas
{
public CGTabsPanel(Container _Owner, Image[] _Sprite, String[] _Tip)
{
 int n=0, q=0;

	try
	{
		Owner=_Owner;
		setLayout(null);
		
		q=_Sprite.length;
		Tab=new CGTab[q];
		Page=new CPage[q];
		for (n=0; n<q; n++)
		{
			Tab[n]=new CGTab(this, _Sprite[n], _Tip[n]);
			Tab[n].N=n; add(Tab[n]);
			Page[n]=new CPage(this); Page[n].setLayout(null);
			Page[n].N=n; add(Page[n]);
		}
		Tab[0].Selected=true;

	}
	catch (Exception exc)
	{
	}
	
}
public void setStyle(int _Style)
{
	try
	{
		for (int n=0; n<Tab.length; n++) ((CGTab)Tab[n]).setStyle(_Style);
	}
	catch(Exception e){}
}
}