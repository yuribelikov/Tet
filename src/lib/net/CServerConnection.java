package lib.net;

import java.net.*;
import java.io.*;
import lib.util.*;

public class CServerConnection extends CConnection
{
 private ServerSocket ServerSocket=null;
public CServerConnection(ServerSocket _ServerSocket)
{
	ServerSocket=_ServerSocket;

	start();
}
protected boolean connect()
{
	try
	{
		Socket=ServerSocket.accept();
Log.log(getName()+" : "+getClass().getName()+".Socket="+Socket);
		OIS=new ObjectInputStream(Socket.getInputStream());
Log.log(getName()+" : "+getClass().getName()+".OIS="+OIS);
		OOS=new ObjectOutputStream(Socket.getOutputStream());
Log.log(getName()+" : "+getClass().getName()+".OOS="+OOS);

		return true;
	}
	catch(Exception e)
	{
		Log.err(getName()+" : "+getClass().getName()+".connect() error : "+e);
	}

	return false;
}
}