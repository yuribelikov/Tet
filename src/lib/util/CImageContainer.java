package lib.util;

import java.awt.*;
import java.applet.*;
import java.util.*;
import lib.util.*;

public class CImageContainer extends Hashtable
{
 public	Applet Owner=null;
public CImageContainer(Applet _Owner)
{
	super();
	
 String[] sa=null;
 Image[] ia=null;
 CImageLoader il=null;

	try
	{
		Owner=_Owner;
		il=new CImageLoader(Owner);
		sa=Util.explode(Owner.getParameter("ImageNames"), ',', true);
		ia=il.getImageForName(Util.explode(Owner.getParameter("ImageFiles"), ',', true));
		for (int i=0; i<sa.length; i++) put(sa[i].toLowerCase(), ia[i]);

	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+"() error : "+e);
	}
}
public Image get(String _key)
{
 Object o=null;
 Image i=null;
 
	try
	{
		o=super.get(_key.toLowerCase());
		if (o==null)
		{
			Log.err(Owner.getClass().getName()+" error : image with key '"+_key+"' not found. Check applet parameters...");
			i=Owner.createImage(1, 1);
		}
		else i=(Image)o;

	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".get() error : "+e);
		if (i==null) i=Owner.createImage(1, 1);
	}

	return i;
}
}