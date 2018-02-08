package lib.gcl;

import java.awt.*;

public class CXSectionsPanel extends ScrollPane implements IGCLClickListener
{
 public Container Owner=null;
 public Panel Area=null;
 public CXSection[] Section=null;
 public CXTab[] Tab=null;
 public String ETip="Click here to Expand Section";
 public String CTip="Click here to Collapse Section";
 
public CXSectionsPanel(Container _Owner, String[] _Caption)
{
 int n=0, q=0;

	try
	{
		Owner=_Owner;
		setBackground(Owner.getBackground());
		getHAdjustable().setUnitIncrement(50);
		getVAdjustable().setUnitIncrement(50);
		
		Area=new Panel();
		add(Area); Area.setBackground(getBackground());
		Area.setLayout(null);

		q=_Caption.length;
		Tab=new CXTab[q];
		Section=new CXSection[q];
		for (n=0; n<q; n++)
		{
			Tab[n]=new CXTab(this, _Caption[n]);
			Tab[n].setTip(ETip);
			Tab[n].N=n; Area.add(Tab[n]);
			Section[n]=new CXSection(this); Section[n].setLayout(null);
			Section[n].N=n; Area.add(Section[n]);
		}

	}
	catch (Exception exc)
	{
	}
	
}
public void expandCollapseTab(CXTab _Tab)
{
 int n=0;

	try
	{
		_Tab.Expanded=!_Tab.Expanded;
		if (_Tab.Expanded) _Tab.setTip(CTip);
		else _Tab.setTip(ETip);

		if (_Tab.Expanded)
		{
			for (n=0; n<Tab.length; n++)
			if (n!=_Tab.N)
			{
				if (_Tab.OnlyOne) Tab[n].Expanded=false;
				else if (Tab[n].OnlyOne) Tab[n].Expanded=false;
			}
		}
		
		layoutComponents();

	}
	catch (Exception exc)
	{
	}
}
public void gcClicked(Event evt)
{
	expandCollapseTab((CXTab)evt.target);
}
public void layoutComponents()
{
 int n=0, w=0, y=0, dy=0;

	try
	{
		doLayout();
		w=Area.getSize().width;
		for (n=0; n<Section.length; n++)
		{
			Tab[n].setBounds(0, y, w, 20);
			y+=20;
			if (Tab[n].Expanded) dy=Section[n].H;
			else dy=0;
			Section[n].setBounds(0, y, w, dy);
			y+=dy;
		}

		Area.setSize(w, y);
		doLayout();
//System.out.println(getInsets());
//		Area.setSize(Area.getSize().width-getInsets().right, Area.getSize().height);
		Area.setSize(getSize().width-getInsets().right-2, Area.getSize().height);
		doLayout();
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
public void setBounds(int x, int y, int w, int h)
{
	super.setBounds(x, y, w, h);
	Area.setSize(getSize().width-getInsets().right-2, getSize().height-getInsets().bottom-2);

	layoutComponents();
}
public void setDescriptionX(int _X)
{
 int n=0;

	try
	{
		for (n=0; n<Tab.length; n++) Tab[n].DescriptionX=_X;
	}
	catch (Exception exc)
	{
	}
}
public void setTips(String _ETip, String _CTip)
{
	try
	{
		ETip=_ETip; CTip=_CTip;
		for (int n=0; n<Tab.length; n++)
			if (Tab[n].Expanded) Tab[n].setTip(CTip);
			else Tab[n].setTip(ETip);
		
	}
	catch (Exception exc)
	{
	}
}
}