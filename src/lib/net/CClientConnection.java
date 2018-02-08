package lib.net;

import lib.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CClientConnection extends CConnection
{
  private String Server = "";
  private int Port = 0;

  public CClientConnection(String _Server, int _Port)
  {
    Server = _Server;
    Port = _Port;

    start();
  }

  protected boolean connect()
  {
    try
    {
      Socket = new Socket(InetAddress.getByName(Server), Port);
      Log.log(getName() + " : " + getClass().getName() + ".Socket=" + Socket);
      OOS = new ObjectOutputStream(Socket.getOutputStream());
      Log.log(getName() + " : " + getClass().getName() + ".OOS=" + OOS);
      OIS = new ObjectInputStream(Socket.getInputStream());
      Log.log(getName() + " : " + getClass().getName() + ".OIS=" + OIS);

      return true;
    }
    catch (Exception e)
    {
      Log.err(getClass().getName() + ".connect() error : " + e);
    }

    return false;
  }
}