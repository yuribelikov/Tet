package lib.util;

import java.awt.*;
import java.util.*;
import java.net.*;

public class ImageContainer
{
 private static Hashtable Images=new Hashtable();
 private static String[] ImageNames={"backButton.gif", "deleteButton.gif", "importButton.gif", "newButton.gif", "okButton.gif", "cancelButton.gif"};
public static Image getImage(String _Name)
{
	return (Image)Images.get(_Name);
}
public static void load(Component _Component)
{
 MediaTracker mt=null;
 URL url=null;
 Image img=null;

	try
	{
		mt=new MediaTracker(_Component);
		String imageDir=System.getProperty("user.dir")+"\\Images\\";
		for (int n=0; n<ImageNames.length; n++)
		{
			try
			{
				url=new URL("file:/"+imageDir+ImageNames[n]);
				img=_Component.getToolkit().getImage(url); mt.addImage(img, n);
				Images.put(ImageNames[n], img);
			}
			catch(Exception e)
			{
				Log.err("ImageContainer.init() image loading error : "+e);
			}
		}
		mt.waitForAll();
	}
	catch(Exception e)
	{
		Log.err("ImageContainer.init() error : "+e);
	}
}
}
