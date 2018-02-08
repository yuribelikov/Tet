package lib.net;

import lib.util.Log;
import lib.util.Util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;

public class CHttpConnection
{
  public int MaxDataSize = 16384;

  private String Host = "", Path = "";
  private Hashtable Parameters = null;

  public CHttpConnection()
  {
    this("");
  }

  public CHttpConnection(String _Host)
  {
    this(_Host, "");
  }

  public CHttpConnection(String _Host, String _Path)
  {
    checkParam();
    setURL(_Host, _Path);
  }

  public void addParameter(String _ParamName, String _ParamValue)
  {
    try
    {
      checkParam();
      Parameters.put(_ParamName, _ParamValue);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".addParameter() error : " + e);
    }
  }

  public InputStream call()
  {
    String params = "";
    URL url = null;

    try
    {
      params = getParametersString();
      if (params.length() > 0) params = "?" + params;

      url = new URL("http://" + getURL() + params);
      return url.openStream();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".call() error : " + e);
    }

    return null;
  }

  private void checkParam()
  {
    if (Parameters == null) Parameters = new Hashtable();
  }

  public void clearParameters()
  {
    try
    {
      checkParam();
      Parameters.clear();
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".clearParameters() error : " + e);
    }
  }

  public String getHost()
  {
    return Host;
  }

  public Hashtable getParameters()
  {
    return Parameters;
  }

  public String getParametersString()
  {
    String str = "", key = "";
    Enumeration en = null;

    try
    {
      checkParam();
      en = Parameters.keys();
      while (en.hasMoreElements())
      {
        key = "" + en.nextElement();
        str += key + "=" + Parameters.get(key) + "&";
      }

      if (str.length() > 0) str = str.substring(0, str.length() - 1);

    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".getParametersString() error : " + e);
    }

    return str;
  }

  public String getPath()
  {
    return Path;
  }

  public String getURL()
  {
    return Host + "/" + Path;
  }

  public void removeParameter(String _ParamName)
  {
    try
    {
      checkParam();
      Parameters.remove(_ParamName);
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".removeParameter() error : " + e);
    }
  }

  public void setHost(String _Host)
  {
    Host = Util.trim(_Host);
  }

  public void setParameters(Hashtable _Parameters)
  {
    Parameters = _Parameters;
    checkParam();
  }

  public void setPath(String _Path)
  {
    Path = Util.trim(_Path);
  }

  public void setURL(String _Host, String _Path)
  {
    setHost(_Host);
    setPath(_Path);
  }

  /*public ObjectInputStream writeObject(Object _Data)
  {
   URL url=null;
   URLConnection conn=null;
   ObjectOutputStream oos=null;


    try
    {
      url=new URL("http://"+getURL());
      conn=url.openConnection();
      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("CONTENT-TYPE", "application/octet-stream");

      oos=new ObjectOutputStream(conn.getOutputStream());
      oos.writeObject(_Data); oos.flush(); oos.close();

      return new ObjectInputStream(conn.getInputStream());
    }
    catch(Exception e)
    {
      Log.err(getClass().getName()+".writeObject() error : "+e);
    }

    return null;
  }*/
  public ObjectInputStream writeObject(Object _Data)
  {
    URL url = null;
    URLConnection conn = null;
    ByteArrayOutputStream bas = null;
    ObjectOutputStream oos = null;
    OutputStream os = null;
    byte[] ba = null;
    String str = "", hex = "";

    try
    {
      url = new URL("http://" + getURL());
      conn = url.openConnection();
      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("CONTENT-TYPE", "application/octet-stream");

      bas = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bas);
      oos.writeObject(_Data);
      oos.flush();
      oos.close();
      ba = bas.toByteArray();
      for (int n = 0; n < ba.length; n++)
      {
        hex = Integer.toHexString(128 + ba[n]);
        if (hex.length() < 2) hex = "0" + hex;
        str += hex;
      }

      System.out.println("Prepare to send " + str.getBytes().length + " bytes");
      if (str.getBytes().length > MaxDataSize)
      {
        Log.err("cannot write object bc data size more than " + MaxDataSize);
        return null;
      }

      os = conn.getOutputStream();
      os.write(str.getBytes());
      os.flush();
      os.close();
      System.out.println("Sent");
      return new ObjectInputStream(conn.getInputStream());
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".writeObject() error : " + e);
    }

    return null;
  }

}