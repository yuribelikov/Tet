package lib.db;

import lib.util.*;

public class CConnector
{
 public static final int DB2_WIN=1;
 public static final int DB2_AS400=2;
 public static final int ORACLE=3; 
 public static final int MSSQL=4;
 public static final String[] AvailableTypes={"UNKNOWN","DB2_WIN","DB2_AS400","ORACLE","MSSQL"};

 public static final int SELECT=101;
 public static final int INSERT=102;
 public static final int UPDATE=103;
 public static final int DELETE=104;

 protected java.sql.Connection Conn=null;
 protected int Type=0, ErrorCode=0;
 public	   int UpdatedRows=0;
 protected String Server="", DB="", Schema="", User="", Password="";
public CConnector(String[] _Params)
{
 	this(_Params[0], _Params[1], _Params[2], _Params[3], _Params[4], _Params[5]);
}
public CConnector(java.applet.Applet _Applet)
{
 	this(_Applet.getParameter("DB_Type"),
			 _Applet.getParameter("DB_Server"),
			 _Applet.getParameter("DB_Name"),
			 _Applet.getParameter("DB_Schema"),
			 _Applet.getParameter("DB_User"),
			 _Applet.getParameter("DB_Password"));
}
public CConnector(String _Params)
{
 	this(Util.explode(_Params, ',', true));
}
public CConnector(String _Type, String _Server, String _DB, String _Schema, String _User, String _Password)
{
 	String str="";
 	int n=0;

	try
	{ 
		for (n=1; n<AvailableTypes.length; n++)
		if (_Type.equalsIgnoreCase(AvailableTypes[n]))
		{
			Type=n; break;
		}
		
		Server=_Server;
		DB=_DB;
		Schema=_Schema;
		User=_User;
		Password=_Password;
		if (Type==DB2_WIN || Type==DB2_AS400 || Type==ORACLE || Type==MSSQL) return;

	}
	catch(Exception e){}

	for (n=1; n<AvailableTypes.length; n++)
	{
		str+=AvailableTypes;
		if (n<AvailableTypes.length-1) str+=", ";
	}
	Log.err("CConnector.CConnector() :\nIncorrect DB_Type parameter :\n"+"DB_Type = "+_Type+"\nAvailable values of DB_Type are : "+str);
}
public void close()
{
	try
	{
		if (Conn==null) return;
		Conn.close();
Log.log("DB connection closed.");
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".close() error : "+e);
	}
}
public boolean connect()
{
 java.util.Properties info=null;
 String dbSource="", str="",driver="";

	try
	{
		ErrorCode=0;
		if (isConnected()) return true;

Log.log("connect...");
		info=new java.util.Properties();
		info.put("user", User); info.put("password", Password);

		switch (Type)
		{
			case MSSQL :
			{
//				connectToMSSQL();
//				str="jdbc:AvenirDriver://"+Server+"/"+DB; 
				str="jdbc:merant:sqlserver://"+Server+";DatabaseName="+DB;
				driver="com.merant.datadirect.jdbc.sqlserver.SQLServerDriver";
//System.out.println(str);				
				break;
			}
			case DB2_WIN :
			{
//				connectToDB2Win();
				str="jdbc:db2://"+Server+"/"+DB; 
				driver="COM.ibm.db2.jdbc.net.DB2Driver";
				break;
			}
			case DB2_AS400 :
			{
//				connectToDB2AS400();
				str="jdbc:as400://"+Server+"/"+DB; 
				driver="com.ibm.as400.access.AS400JDBCDriver";
				break;
			}
			case ORACLE :
			{
//				connectToOracle();
				str="jdbc:oracle:thin:@"+Server+':'+DB; 
				driver="oracle.jdbc.driver.OracleDriver";
				break;
			}
			default : { ErrorCode=-1; return false; }
		}

		java.sql.DriverManager.registerDriver((java.sql.Driver) Class.forName(driver).newInstance());
		
		
Log.log("connect to : "+str);
Log.log("connect as <"+info.getProperty("user")+"> with password <"+info.getProperty("password")+">");
		Conn=java.sql.DriverManager.getConnection(str, info);

		return true;
	}
	catch (Throwable e)
	{
		ErrorCode=-1;
		Log.err(getClass().getName()+".connect() error : "+e);
		if (e instanceof java.sql.SQLException)
		{
			ErrorCode=((java.sql.SQLException)e).getErrorCode();
			Log.err(getClass().getName()+".connect() error(SQL) : "+ErrorCode);
		}
	}

	return false;
}
public java.sql.ResultSet doSQL(int _Command, String _Tables)
{
	return doSQL(_Command, _Tables, "", "", "", "");
}
public java.sql.ResultSet doSQL(int _Command, String _Tables, String _Fields)
{
	return doSQL(_Command, _Tables, _Fields, "", "", "");
}
public java.sql.ResultSet doSQL(int _Command, String _Tables, String _Fields, String _Values)
{
	return doSQL(_Command, _Tables, _Fields, _Values, "", "");
}
public java.sql.ResultSet doSQL(int _Command, String _Tables, String _Fields, String _Values, String _Where)
{
	return doSQL(_Command, _Tables, _Fields, _Values, _Where, "");
}
public java.sql.ResultSet doSQL(int _Command, String _Tables, String _Fields, String _Values, String _Where, String _Misc)
{
 	String sql="", tables="", fields="", values="", where="", misc="", s1="", s2="";
 	String[] ta=null, fa=null, va=null;
 	int n=0;

	try
	{
		tables=_Tables; fields=_Fields; values=_Values; where=_Where; misc=_Misc;

		if (Schema.length()>0)		// add schema to tables
		{
			ta=Util.explode(tables, ',', true); tables="";
			for (n=0; n<ta.length; n++)
			{
				tables+=Schema+'.'+ta[n];
				if (n<ta.length-1) tables+=',';
			}
		}

		switch (_Command)				// create sql
		{
			case SELECT :
			{
				sql="SELECT "+fields+" FROM "+tables; break;
			}
			case INSERT :
			{
				sql="INSERT INTO "+tables+" ("+fields+") VALUES ("+values+")"; break;
			}
			case UPDATE :
			{
				fa=Util.explodeWithQuotes(fields, ',', true); 
				va=Util.explodeWithQuotes(values, ',', false);
				sql="UPDATE "+tables+" SET ";
				for (n=0; n<fa.length; n++)
				{
					sql+=fa[n]+'='+va[n];
					if (n<fa.length-1) sql+=',';
				}
				break;
			}
			case DELETE :
			{
				sql="DELETE FROM "+tables; break;
			}
		}

		if (where.length()>0) sql+=" WHERE "+where;
		if (misc.length()>0) sql+=" "+misc;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".doSQL(Statement construction error) error : "+e);
	}
	if(_Command==SELECT) return doSQL(sql);
	else doUpdate(sql);
	
	return null;
}
public java.sql.ResultSet doSQL(String _SQL)
{
	java.sql.Statement stmt=null;
	java.sql.ResultSet rs=null;

	try
	{
		if (!connect()) return null;

		ErrorCode=0;
Log.log("SQL="+_SQL);
		stmt=Conn.createStatement();
		rs=stmt.executeQuery(_SQL);
	}
	catch (java.sql.SQLException e)
	{
Log.err("ERROR SQL="+_SQL);
		ErrorCode=e.getErrorCode();
		Log.err(getClass().getName()+".doSQL(SQL) error : "+e);
	}
	catch (Exception e)
	{
Log.err("ERROR SQL="+_SQL);
		ErrorCode=-1;
		Log.err(getClass().getName()+".doSQL() error : "+e);
	}
	return rs;
}
public void doUpdate(String _SQL)
{
	java.sql.Statement stmt=null;	

	try
	{
		if (!connect()) return;

		ErrorCode=0;
Log.log("UPDATE="+_SQL);
//		Conn.setAutoCommit(true);
		stmt=Conn.createStatement();
		UpdatedRows=stmt.executeUpdate(_SQL);
		Conn.commit();
Log.log("Updated Rows : "+UpdatedRows);
		stmt.close();
	}
	catch (java.sql.SQLException e)
	{
Log.err("ERROR SQL="+_SQL);
		ErrorCode=e.getErrorCode();
		Log.err(getClass().getName()+".doUpdate(SQL) error : "+e);
	}
	catch (Exception e)
	{
Log.err("ERROR SQL="+_SQL);
		ErrorCode=-1;
		Log.err(getClass().getName()+".doUpdate() error : "+e);
	}

}
public int getDBType()
{
	return Type;
}
public int getErrorCode()
{
	return ErrorCode;
}
public String getSchema()
{
	return Schema;
}
public boolean isConnected()
{
	try
	{
		return Conn!=null && !Conn.isClosed();
	}
	catch(Exception e)
	{
		return false;
	}
}
public String toString()
{
 String str="";

	str="DB_Connector :\n";
	str+="Type="+AvailableTypes[Type]+'\n';
	str+="Server="+Server+'\n';
	str+="DB="+DB+'\n';
	str+="Schema="+Schema+'\n';
	str+="User="+User+'\n';
	str+="Password="+Password+'\n';
	str+="Connection="+Conn+'\n';

	return str;
}
}
