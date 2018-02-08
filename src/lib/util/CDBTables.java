package lib.util;

public class CDBTables extends java.util.Hashtable
{
	private String[] DbLogin;


public CDBTables(java.applet.Applet _applet)
{
	super();
	
	java.net.URL url=null;
 	java.io.BufferedReader br=null;
 	String text="", path="", s0="";
	try
	{
		path=_applet.getParameter("PropertiesFilePath")+"prp_DBAgent.txt";

		if(_applet.getParameter("PropertiesFilePath").startsWith("http"))
			url=new java.net.URL(path);
		else
			url=new java.net.URL(_applet.getCodeBase().getProtocol(),_applet.getCodeBase().getHost(),path);
//		url=new java.net.URL(path);
//Log.sop("url="+url);
		br=new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
		while ((s0=br.readLine())!=null) text+=s0;
		text+="&key=";
		path=_applet.getParameter("PropertiesFilePath")+"prp_DBKey.txt";
		if(_applet.getParameter("PropertiesFilePath").startsWith("http"))
			url=new java.net.URL(path);
		else
			url=new java.net.URL(_applet.getCodeBase().getProtocol(),_applet.getCodeBase().getHost(),path);
//		url=new java.net.URL(path);
		br=new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
		while ((s0=br.readLine())!=null) text+=s0;

		if(_applet.getParameter("PropertiesFilePath").startsWith("http"))
			createData(new java.net.URL(_applet.getParameter("PropertiesFilePath")+text),Util.explode(text,"=",true)[1]);
		else
			createData(new java.net.URL(_applet.getCodeBase().getProtocol(),_applet.getCodeBase().getHost(),_applet.getParameter("PropertiesFilePath")+text),Util.explode(text,"=",true)[1]);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+"() error : "+e);
	}
	
}
public CDBTables(String _url)
{
	super();
	
	java.net.URL url=null;
 	java.io.BufferedReader br=null;
 	String text="", path="", s0="";
	try
	{
		path=_url+"prp_DBAgent.txt";
		url=new java.net.URL(path);
//		url=new java.net.URL(path);
//Log.sop("url="+url);
		br=new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
		while ((s0=br.readLine())!=null) text+=s0;
		text+="&key=";
		path=_url+"prp_DBKey.txt";
		url=new java.net.URL(path);
//		url=new java.net.URL(path);
		br=new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
		while ((s0=br.readLine())!=null) text+=s0;
		createData(new java.net.URL(_url+text),Util.explode(text,"=",true)[1]);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+"() error : "+e);
	}
	
}
public void createData(java.net.URL _URL,String _key)
{
	
 java.io.InputStream is=null;
 String text="";
 int n=0, result=0, size=0, n2=0;
 byte[] ba=null, ba2=null;
 String[] sa=null, ssa=null;

	try
	{
		is=_URL.openStream();
		size=1000; ba=new byte[size];
		try
		{
			while (result>=0)
			{
				result=is.read();
				if (result<0) break;
				while (result==0) result=is.read();
				ba[n++]=(byte)result;
				if (n>=size)		// grow array
				{
					ba2=ba; size=ba2.length+1000; ba=new byte[size];
					System.arraycopy(ba2, 0, ba, 0, ba2.length);
				}
			}
		}
		catch(Exception e){Log.err("Error in "+getClass().getName()+"::CDBTables() "+e);}
		is.close();
		ba2=ba; ba=new byte[n];
		System.arraycopy(ba2, 0, ba, 0, n);
		text=new String(ba);

String start="<BODY TEXT=\"000000\">";
String finish="</BODY>";
text=text.substring(text.indexOf(start)+start.length()+1,text.indexOf(finish));

//		text = new String(Util.coder(text.getBytes(),_key));
		byte[] b=new byte[text.length()/2];
		for(int i=0;i<b.length;i++)
		{
			b[i]=(byte) Integer.parseInt(text.substring(i*2,i*2+2),16);
//System.out.println(text.substring(i*2,i*2+2)+" "+b[i]);			
		}
//System.out.println(text);
		text=new String(b);
		text = new String(Util.decoder(text.getBytes(),_key.getBytes()))+"\n";
//System.out.println(text);		
//----dbProfile
		n=text.indexOf("~~DBPROFILE~~");
		if(n>-1)
		{
			int end=text.indexOf("~~TABLES~~");
			String Profile=text.substring(n+13,end);
			DbLogin=Util.explode(Profile,",",true);						
		}		
//----dbTables		
		text=text.toUpperCase();
		n=text.indexOf("~~TABLES~~");
		if (n<0) return;
		n+=10; n2=text.indexOf('<', n);
		if (n2<0) n2=text.length()-1;
		text=text.substring(n, n2);

		sa=Util.explode(text, ',', true);
		for (n=0; n<sa.length; n++)
		{
			if (sa[n].length()==0) continue;
			ssa=Util.explode(sa[n], " - ", true);
			put(ssa[0], ssa[1]);
		}


	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".createData() error : "+e);
	}

	if (size()==0)
		Log.err("WARNING !!! \n    List of db tables is empty. Check the notes agent 'DBTablesList'. Maybe agent should be re-signed.");
	
}
public String get$(String _key)
{
	return ""+get(_key.toUpperCase());
}
public String get(String _key)
{
	return ""+super.get(_key.toUpperCase());
}
public String[] getDbLogin()
{
//	return new String[]{"MSSQL","1.0.0.22:1433","JAVA_TEST","","yurib","ccc"};
	return DbLogin;
}
}