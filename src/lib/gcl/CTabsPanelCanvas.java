package lib.gcl;

import java.awt.*;

public class CTabsPanelCanvas extends Panel implements IGCLClickListener
{
 public Container Owner=null;
 public CTabCanvas[] Tab=null;
 public CPage[] Page=null;
 protected int SelectedTab=0;
public void gcClicked(Event evt)
{
 IGCLClickListener l=null;

	try
	{
		selectTab((CTabCanvas)evt.target);
		
		l=(IGCLClickListener)Owner;
		l.gcClicked(evt);
	}
	catch(Exception e1){}
}
public void layoutComponents(double _D)
{
 int n=0, q=0, w=0;

	try
	{
		q=Page.length;
		w=(int)(getSize().width/(_D*q));
		for (n=0; n<q; n++) Tab[n].setBounds(n*w, 0, w, 20);
		for (n=0; n<q; n++) Page[n].setBounds(0, 20, getSize().width, getSize().height-20);

	}
	catch (Exception exc)
	{
	}
}
public void paint()
{
	paint(getGraphics());
}
public void paint(Graphics g)
{
 int w=0, h=0, x=0, y=0, n=0, q=0;
 
	try
	{
		q=Tab.length;
		for (n=0; n<q; n++) Tab[n].paint();
		
	}
	catch(Exception e1){}

}
public void selectTab(CTabCanvas _Tab)
{
 int n=0;

	try
	{
		if (SelectedTab==_Tab.N) return;
		
		for (n=0; n<Tab.length; n++) Tab[n].Selected=(n==_Tab.N);
		SelectedTab=_Tab.N;
		add(Page[SelectedTab], 0);
		paint();

	}
	catch (Exception exc)
	{
	}
}
}