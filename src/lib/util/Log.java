package lib.util;

import java.util.*;

public class Log
{
 public static final int CONSOLE=1;
 public static final int VECTOR_BUFFER=2;

 private static final int SOP_TO_CONSOLE=101;
 private static final int SOP_TO_VECTOR=102;
 private static final int LOG_TO_CONSOLE=201;
 private static final int LOG_TO_VECTOR=202;
 private static final int ERR_TO_CONSOLE=301;
 private static final int ERR_TO_VECTOR=302;
 
 public static boolean LogEnabled=false;
 public static boolean ErrOutEnabled=true;
 public static int Target=CONSOLE;
 public static boolean ShowTime=false;
 public static String Prefix="";
 

 private static java.text.SimpleDateFormat Formatter=new java.text.SimpleDateFormat("yy-MM-dd HH:mm:ss");
 private static Vector V=new Vector();

 private static ILogListener LogListener=null;
public static void err(Object _O)
{
	switch (Target)
	{
		case CONSOLE : { errToConsole(_O); break; }
		case VECTOR_BUFFER : { errToVector(_O); break; }
	}
}
public static void errToConsole(Object _O)
{
	out(ERR_TO_CONSOLE, _O);
}
public static void errToVector(Object _O)
{
	out(ERR_TO_VECTOR, _O);
}
public static String get()
{
	switch (Target)
	{
		case VECTOR_BUFFER : return getFromVector();
	}

	return "";
}
public static Object[] getArray()
{
	switch (Target)
	{
		case VECTOR_BUFFER : return getArrayFromVector();
	}

	return null;
}
public static Object[] getArrayFromVector()
{
 Object[] oa=null;

	synchronized (V)
	{
		oa=new Object[V.size()];
		V.copyInto(oa); V.removeAllElements();
	}

	return oa;
}
public static String getFromVector()
{
 int n=0;
 String str="";

	synchronized (V)
	{
		for (n=0; n<V.size(); n++) str+=(V.elementAt(n)+"\n");
		V.removeAllElements();
	}

	return str;
}
public static void log(Object _O)
{
	switch (Target)
	{
		case CONSOLE : { logToConsole(_O); break; }
		case VECTOR_BUFFER : { logToVector(_O); break; }
	}
}
public static void logToConsole(Object _O)
{
	out(LOG_TO_CONSOLE, _O);
}
public static void logToVector(Object _O)
{
	out(LOG_TO_VECTOR, _O);
}
private static void out(int _Where, Object _O)
{
 String str="", prefix="";
 Date d=null;
 
	prefix+=Prefix;															// prefix creation
	if (prefix.length()>0) prefix+=" : ";
	if (ShowTime)
	{
		d=new Date();
		prefix+=(Formatter.format(d)+" : ");
	}

	str=prefix+_O;
	switch (_Where)
	{
		case SOP_TO_CONSOLE :
		{
			System.out.println(str); break;
		}
		case SOP_TO_VECTOR :
		{
			toV(str); break;
		}

		case LOG_TO_CONSOLE :
		{
			if (LogEnabled) System.out.println(str); break;
		}
		case LOG_TO_VECTOR :
		{
			if (LogEnabled) toV(str); break;
		}

		case ERR_TO_CONSOLE :
		{
			if (ErrOutEnabled) System.out.println(str); break;
		}
		case ERR_TO_VECTOR :
		{
			if (ErrOutEnabled) toV(str); break;
		}
	}
}
public static void setLogListener(ILogListener _LogListener)
{
	LogListener=_LogListener;
}
public static void sop(Object _O)
{
	switch (Target)
	{
		case CONSOLE : { sopToConsole(_O); break; }
		case VECTOR_BUFFER : { sopToVector(_O); break; }
	}
}
public static void sopToConsole(Object _O)
{
	out(SOP_TO_CONSOLE, _O);
}
public static void sopToVector(Object _O)
{
	out(SOP_TO_VECTOR, _O);
}
private static void toV(String _Str)
{
	synchronized (V)
	{
		V.addElement(_Str);
		try
		{
			if (LogListener!=null) LogListener.logUpdated();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
}