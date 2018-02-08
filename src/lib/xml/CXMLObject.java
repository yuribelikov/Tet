package lib.xml;

import java.util.*;
//import lib.util.*;

public class CXMLObject
{
 public CXMLObject Parent=null;
 public String Name=null, Body=null;
 public Hashtable Attributes=null;
 public Vector Children=null;
public String toString()
{
/*
 public CXMLObject Parent=null;
 public String Name=null, Body=null;
 public Hashtable Attributes=null;
 public Vector Children=null;
*/
	try
	{
		StringBuffer sb=new StringBuffer();
		if (Parent!=null && Parent.Parent!=null)
		{
			CXMLObject parent=Parent.Parent;
			while (parent!=null)
			{
				parent=parent.Parent; sb.append("  ");
			}
		}
		String offset=sb.toString();
		sb=new StringBuffer();
		if (Name!=null)			// for root object
		{
			sb.append(offset); sb.append('<'); sb.append(Name);
			if (Attributes!=null)
			{
				Enumeration en=Attributes.keys();
				while (en.hasMoreElements())
				{
					String key=(String)en.nextElement();
					sb.append(' '); sb.append(key); sb.append("=\"");
					sb.append((String)Attributes.get(key)); sb.append('"');
				}
			}
			sb.append('>');
			if (Body!=null && Body.length()>0)
			{
//				sb.append("\r\n  "); sb.append(offset); 
				sb.append(Body);// sb.append("\r\n");
			}
		}
		if (Children!=null)
		{
			for (int n=0; n<Children.size(); n++)
			{
				if (Parent!=null) sb.append("\r\n");
				sb.append(Children.elementAt(n));
				if (n<Children.size()-1 || Parent!=null) sb.append("\r\n");
			}
		}
		if (Name!=null)			// for root object
		{
			sb.append(offset); sb.append("</"); sb.append(Name); sb.append('>');
		}
		return sb.toString();
	}
	catch(Exception e)
	{
		System.out.println(getClass().getName()+".toString() error: "+e);
	}
	return null;
}
}
