package lib.util;

import java.applet.Applet;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

public class CStringResources extends Hashtable
{
  public final String DEFAULT_LANGUAGE = "en";
  public Applet Owner = null;
  public Frame FrameOwner = null;

  public CStringResources(Applet _Owner)
  {
    super();

    Owner = _Owner;

    URL url = null;
    InputStream is = null;
    String text = "";
    int n = 0, result = 0, size = 0;
    byte[] ba = null, ba2 = null;
    String[] sa = null, ssa = null;

    try
    {
      if (Util.trim(Owner.getParameter("PropertiesFilePath")).toUpperCase().startsWith("HTTP:"))
        url = new URL(Owner.getParameter("PropertiesFilePath") + Owner.getName() + "." + Owner.getLocale().getLanguage());
      else
        url = new URL(Owner.getCodeBase().getProtocol(),
                Owner.getCodeBase().getHost() + getPortSuffix(),
                Owner.getParameter("PropertiesFilePath") +
                        Owner.getName() + "." + Owner.getLocale().getLanguage());
      try
      {
        is = url.openStream();
      }
      catch (FileNotFoundException e)
      {
        if (Util.trim(Owner.getParameter("PropertiesFilePath")).toUpperCase().startsWith("HTTP:"))
          url = new URL(Owner.getParameter("PropertiesFilePath") + Owner.getName() + "." + DEFAULT_LANGUAGE);
        else
          url = new URL(Owner.getCodeBase().getProtocol(),
                  Owner.getCodeBase().getHost() + getPortSuffix(),
                  Owner.getParameter("PropertiesFilePath") +
                          Owner.getName() + "." + DEFAULT_LANGUAGE);

        is = url.openStream();
      }
      size = 1000;
      ba = new byte[size];
      try
      {
        while (result >= 0)
        {
          result = is.read();
          if (result < 0) break;
          while (result == 0) result = is.read();
          ba[n++] = (byte) result;
          if (n >= size)    // grow array
          {
            ba2 = ba;
            size = ba2.length + 1000;
            ba = new byte[size];
            System.arraycopy(ba2, 0, ba, 0, ba2.length);
          }
        }
      }
      catch (Exception e)
      {
      }
      ba2 = ba;
      ba = new byte[n];
      System.arraycopy(ba2, 0, ba, 0, n);
      text = new String(ba);

      sa = lib.util.Util.explode(text, '\n', true);
      for (n = 0; n < sa.length; n++)
      {
        try
        {
          ssa = lib.util.Util.explode(sa[n], '=', true);
          put(ssa[0], ssa[1]);
        }
        catch (Exception e)
        {
        }
      }

    }
    catch (Exception e)
    {
    }
  }

  public CStringResources(Frame _Owner, String strURL)
  {
    super();

    FrameOwner = _Owner;

    URL url = null;
    InputStream is = null;
    String text = "";
    int n = 0, result = 0, size = 0;
    byte[] ba = null, ba2 = null;
    String[] sa = null, ssa = null;

    try
    {
      url = new URL(strURL);
      try
      {
        is = url.openStream();
      }
      catch (FileNotFoundException e)
      {
        url = new URL(strURL.substring(0, strURL.lastIndexOf(".") + 1) + DEFAULT_LANGUAGE);
        is = url.openStream();
      }
      size = 1000;
      ba = new byte[size];
      try
      {
        while (result >= 0)
        {
          result = is.read();
          if (result < 0) break;
          while (result == 0) result = is.read();
          ba[n++] = (byte) result;
          if (n >= size)    // grow array
          {
            ba2 = ba;
            size = ba2.length + 1000;
            ba = new byte[size];
            System.arraycopy(ba2, 0, ba, 0, ba2.length);
          }
        }
      }
      catch (Exception e)
      {
      }
      ba2 = ba;
      ba = new byte[n];
      System.arraycopy(ba2, 0, ba, 0, n);
      text = new String(ba);

      sa = lib.util.Util.explode(text, '\n', true);
      for (n = 0; n < sa.length; n++)
      {
        try
        {
          ssa = lib.util.Util.explode(sa[n], '=', true);
          put(ssa[0], ssa[1]);
        }
        catch (Exception e)
        {
        }
      }

    }
    catch (Exception e)
    {
      System.out.println("Error in lib.util.CStringResources::CDtringResources() " + e);
    }
  }

  public String get$(String _key)
  {
    return "" + get(_key);
  }

  public String get(String _key)
  {
    return "" + super.get(_key);
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
}
