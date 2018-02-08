package lib.net;

import lib.util.Log;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CConnection extends Thread
{
  protected Socket Socket = null;
  protected ObjectInputStream OIS = null;
  protected ObjectOutputStream OOS = null;

  protected IObjectStreamListener OSL = null;

  protected boolean IsConnected = false;
  protected boolean IsAlive = true;
  protected boolean IsClosed = false;

  public void close()
  {
    Log.log(getName() + " : " + getClass().getName() + ".close() : closing...");

    IsAlive = false;
    try
    {
      OIS.close();
      OOS.close();
    }
    catch (Exception e)
    {
      if (IsConnected) Log.err(getName() + " : " + getClass().getName() + ".close() : streams closing error  : " + e);
    }

    try
    {
      Socket.close();
    }
    catch (Exception e)
    {
      if (IsConnected) Log.err(getName() + " : " + getClass().getName() + ".close() : socket closing error  : " + e);
    }

    IsConnected = false;
    IsClosed = true;
    Log.log(getName() + " : " + getClass().getName() + ".close() : closed successfully.");
  }

  protected boolean connect()
  {
    return true;
  }

  public Socket getSocket()
  {
    return Socket;
  }

  public boolean isClosed()
  {
    return IsClosed;
  }

  public boolean isConnected()
  {
    return IsConnected;
  }

  // this method can be overlapped
  public void objectReceived(Object _O)
  {
    Log.log(getName() + " : " + getClass().getName() + ".objectReceived()=" + _O);
  }

  public void run()
  {
    Object o = null;

    try
    {
      if (connect())
      {
        Log.log(getName() + " : " + getClass().getName() + ".run() : receiver started.");
        IsConnected = true;
        while (IsAlive)
        {
          o = OIS.readObject();
          if (OSL != null) OSL.objectReceived(this, o);
          else objectReceived(o);
        }
      }
    }
    catch (EOFException e)
    {
      Log.log(getName() + " : " + getClass().getName() + ".run() connection lost.");
    }
    catch (Exception e)
    {
      Log.err(getName() + " : " + getClass().getName() + ".run() error : " + e);
    }
    if (IsAlive) close();
  }

  public void sendObject(Object _O)
  {
    try
    {
      OOS.writeObject(_O);
      OOS.flush();
    }
    catch (Exception e)
    {
      Log.err(getName() + " : " + getClass().getName() + ".sendObject() error : " + e);
    }
  }

  public void setObjectListener(IObjectStreamListener _OSL)
  {
    OSL = _OSL;
  }
}
