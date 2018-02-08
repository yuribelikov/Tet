package bel.tetris.container;

import java.awt.*;
import java.util.*;
import java.net.*;
import lib.util.*;

public class ImageContainer
{
 private static Hashtable Images=new Hashtable();
public static Image getImage(String _Name)
{
	return (Image)Images.get(_Name);
}
public static void load(Component _Component)
{
 String[] images={"icon.gif", "interchange.gif", "okButton.gif", "cancelButton.gif"};
 MediaTracker mt=null;
 URL url=null;
 Image img=null;

	try
	{
		mt=new MediaTracker(_Component);
		String currentDir=System.getProperty("user.dir")+"\\";
		for (int n=0; n<images.length; n++)
		{
			try
			{
				url=new URL("file:/"+currentDir+images[n]);
				img=_Component.getToolkit().getImage(url); mt.addImage(img, n);
				Images.put(images[n], img);
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
