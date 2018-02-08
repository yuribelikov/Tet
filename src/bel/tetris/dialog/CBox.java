package bel.tetris.dialog;

import java.awt.*;
import java.awt.event.*;
import lib.gcl.*;
import lib.util.*;
import bel.tetris.event.*;

public class CBox extends Dialog implements IEventReceiver
{
 protected int W=0, H=0;
 protected boolean Result=false;

 protected CEventListener EventListener=null;
 protected CGButton OkButton=null, CancelButton=null;
public CBox()
{
	super(new Frame(), "", true);
}
protected void create()
{
 	try
 	{
		setLayout(null); setBackground(new Color(210, 210, 210));
		setFont(new Font("Dialog", 0, 14));
		EventListener=new CEventListener(this);
		addWindowListener((WindowListener)EventListener);
		
		setResizable(false);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".create() : "+e);
	}
}
public void dispatchEvent(CEvent _Evt)
{
	try
	{
		if (_Evt.getName().equals("ActionPerformed"))
		{
			if (_Evt.getSourceName().equals("OkButton"))
			{
				Result=true; dispose();
			}
			else
			if (_Evt.getSourceName().equals("CancelButton")) dispose();
		}
		else
		if (_Evt.getName().equals("WindowClosing")) dispose();

	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".dispatchEvent() error : "+e);
	}
}
protected void showSelf()
{
 Dimension d=getToolkit().getScreenSize();
 int x=0, y=0, w=0, h=0;

	w=W; h=H;
	x=(d.width-w)/2; y=(d.height-h)/2;
	if (x<0) x=0; if (y<0) y=0;
	setBounds(x, y, w, h);
	show();
}
}
