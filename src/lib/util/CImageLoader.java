package lib.util;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;

public class CImageLoader
{
  public Applet Owner = null;

  public CImageLoader(Applet _Owner)
  {
    Owner = _Owner;
  }

  public Image[] getImage(String[] _SA)
  {
    MediaTracker mt = null;
    URL url = null;
    String imagesFilePath = "";
    Image[] img = null;
    int q = 0, n = 0;

    try
    {
      imagesFilePath = Owner.getParameter("ImagesFilePath");
      if (imagesFilePath == null) imagesFilePath = "";
      mt = new MediaTracker(Owner);
      q = _SA.length;
      img = new Image[q];
      for (n = 0; n < q; n++)
      {
        try
        {
          url = new URL(Owner.getCodeBase().getProtocol(), Owner.getCodeBase().getHost() + getPortSuffix(),
                  imagesFilePath + Owner.getParameter(_SA[n]));
          img[n] = Owner.getImage(url);
          mt.addImage(img[n], n);
        }
        catch (Exception e0)
        {
        }
      }
      mt.waitForAll();

      return img;
    }
    catch (Exception exc)
    {
    }

    return null;
  }

  public Image getImage(String _Str)
  {
    MediaTracker mt = null;
    URL url = null;
    String imagesFilePath = "", host = "";
    Image img = null;
    try
    {
      imagesFilePath = Owner.getParameter("ImagesFilePath");
      if (imagesFilePath == null)
        imagesFilePath = "";
      imagesFilePath += Owner.getParameter(_Str);
      mt = new MediaTracker(Owner);
      host = Owner.getCodeBase().getHost() + getPortSuffix();
      url = new URL(Owner.getCodeBase().getProtocol(), host, imagesFilePath);
      img = Owner.getImage(url);
      mt.addImage(img, 0);
      mt.waitForID(0);
      if (mt.isErrorID(0))
        Log.err("Image file not found (stat=" + status2str(mt.statusID(0, false)) //
                + "):\"" + imagesFilePath + _Str + "\" on \"" + host + "\"");
      return img;
    }
    catch (Exception exc)
    {
      Log.err("Exception in CImageLoader::getImage(" + _Str + ") : " + exc);
    }
    return null;
  }

  public Image[] getImageForName(String[] _SA)
  {
    MediaTracker mt = null;
    URL url = null;
    String imagesFilePath = "", host = "";
    Image[] img = null;
    int q = 0, n = 0;
    try
    {
      imagesFilePath = Owner.getParameter("ImagesFilePath");
      if (imagesFilePath == null)
        imagesFilePath = "";
      mt = new MediaTracker(Owner);
      q = _SA.length;
      img = new Image[q];
      host = Owner.getCodeBase().getHost() + getPortSuffix();
      for (n = 0; n < q; n++)
      {
        try
        {
          url = new URL(Owner.getCodeBase().getProtocol(), host, imagesFilePath + _SA[n]);
          img[n] = Owner.getImage(url);
          mt.addImage(img[n], n);
          mt.waitForID(n);
          if (mt.isErrorID(n))
            Log.err("Image file not found (stat=" + status2str(mt.statusID(n, false)) //
                    + "):\"" + imagesFilePath + _SA[n] + "\" on \"" + host + "\"");
        }
        catch (Exception e0)
        {
        }
      }
      return img;
    }
    catch (Exception exc)
    {
    }
    return null;
  }

  public Image getImageForName(String _Str)
  {
    MediaTracker mt = null;
    URL url = null;
    String imagesFilePath = "", host = "";
    Image img = null;
    try
    {
      imagesFilePath = Owner.getParameter("ImagesFilePath");
      if (imagesFilePath == null)
        imagesFilePath = "";
      mt = new MediaTracker(Owner);
      host = Owner.getCodeBase().getHost() + getPortSuffix();

      url = new URL(Owner.getCodeBase().getProtocol(), host //
              , imagesFilePath + _Str);
      img = Owner.getImage(url);
      mt.addImage(img, 0);
      mt.waitForID(0);
      if (mt.isErrorID(0))
        Log.err("Image file not found (stat=" + status2str(mt.statusID(0, false))//
                + "):\"" + imagesFilePath + _Str + "\" on \"" + host + "\"");
      return img;
    }
    catch (Exception exc)
    {
      Log.err("Exception in CImageLoader::getImageForName(" + _Str + ") : " + exc);
    }
    return null;
  }

  /**
   * Returns a string of colon followed by port number of codebase URL.
   * If exception occures or port number is less then 1,
   * returns empty string.
   *
   * @author Sergey N. Vasiliev
   */
  private String getPortSuffix()
  {
    String port = "";
    int iPort = 0;

    try
    {
      iPort = Owner.getCodeBase().getPort();
      if (iPort > 0) port = ":" + iPort;
    }
    catch (Exception e)
    {
    }
    return port;
  }

  private static String status2str(int _status)
  {
    switch (_status)
    {
      case MediaTracker.ABORTED:
        return "ABORTED";
      case MediaTracker.COMPLETE:
        return "COMPLETE";
      case MediaTracker.ERRORED:
        return "ERRORED";
      case MediaTracker.LOADING:
        return "LOADING";
      case 0:
        return "NOT STARTED";
      default:
        return "UNKNOWN(" + _status + ")";
    }
  }
}
