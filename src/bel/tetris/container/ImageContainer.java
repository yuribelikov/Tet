package bel.tetris.container;

import lib.util.Log;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class ImageContainer
{
  private static Hashtable images = new Hashtable();


  public static Image getImage(String _Name)
  {
    return (Image) images.get(_Name);
  }

  public static void load(Component component)
  {
    String[] images = {"icon.gif", "interchange.gif", "okButton.gif", "cancelButton.gif"};
    MediaTracker mt = null;
    URL url = null;
    Image img = null;

    try
    {
      mt = new MediaTracker(component);
      String currentDir = System.getProperty("user.dir") + "\\";
      for (int n = 0; n < images.length; n++)
      {
        try
        {
          url = new URL("file:/" + currentDir + images[n]);
          img = component.getToolkit().getImage(url);
          mt.addImage(img, n);
          ImageContainer.images.put(images[n], img);
        }
        catch (Exception e)
        {
          Log.err("ImageContainer.init() image loading error : " + e);
        }
      }
      mt.waitForAll();
    }
    catch (Exception e)
    {
      Log.err("ImageContainer.init() error : " + e);
    }
  }
}
