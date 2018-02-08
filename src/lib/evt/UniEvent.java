package lib.evt;

import java.awt.*;
import java.awt.event.*;

public class UniEvent
{
 private String Name=null;
 private Object Data=null;
 private Object Source=null;
 private String SourceName=null;
public UniEvent(String _Name)
{
	this(_Name, null);
}
public UniEvent(String _Name, Object _Data)
{
	super();
	Name=_Name; Data=_Data;

	if (Data!=null)
	{
		if (Data instanceof AWTEvent)
		{
			Source=((AWTEvent)Data).getSource();
		}
		else
		if (Data instanceof Event)
		{
			Source=((Event)Data).target;
		}
	}

	if (Source!=null)
	{
		if (Source instanceof Component)
		{
			SourceName=((Component)Source).getName();
		}
		else
		if (Source instanceof MenuComponent)
		{
			SourceName=((MenuComponent)Source).getName();
		}
	}
}
public Object getData()
{
	return Data;
}
public String getName()
{
	return Name;
}
public Object getSource()
{
	return Source;
}
public String getSourceName()
{
	return SourceName;
}
public String toString()
{
	return "CEvent : "+Name+", SourceName="+SourceName+", Source=["+Source+"], Data=["+Data+"]";
}
}
