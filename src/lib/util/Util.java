package lib.util;

//	This class is util class
import java.util.*;

public class Util
{
public static byte[] coder(byte[] _oldArr,byte[] _key)
{
	byte[] code=new byte[_oldArr.length*2];

	int pos=0;
	for(int i=0;i<_oldArr.length;i++)
	{
		if(pos==_key.length) pos=0;

		int data=_oldArr[i]*_key[pos];
		int count=0;
		while(data>127) {count++;data-=127;}

		code[i*2]=(byte) data;
		code[i*2+1]=(byte) count;
		pos++;
	}
	return code;	
}
// count _C in _Str
public static int count(String _Str, char _C)
{
 int pos1=0, pos2=0, len=0, n=0;

	if (_Str==null) return 0;

	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_C, pos1);
		if (pos2<0 || pos2>=len) break;
		n++; pos1=pos2+1;
	}

	return n;
}
// count _C in _Str
public static int count(String _Str, int _C)
{
 int pos1=0, pos2=0, len=0, n=0;

	if (_Str==null) return 0;

	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_C, pos1);
		if (pos2<0 || pos2>=len) break;
		n++; pos1=pos2+1;
	}

	return n;
}
// count _C in _Str
public static int count(String _Str, String _S)
{
 int pos1=0, pos2=0, len=0, n=0;

	if (_Str==null) return 0;

	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_S, pos1);
		if (pos2<0 || pos2>=len) break;
		n++; pos1=pos2+_S.length();
	}

	return n;
}
public static byte[] decoder(byte[] _oldArr,byte[] _key)
{
	byte[] decode=new byte[_oldArr.length/2];

	int pos=0;
	for(int i=0;i<_oldArr.length/2;i++)
	{
		if(pos==_key.length) pos=0;

		int data=_oldArr[i*2];
		int count=_oldArr[i*2+1];
		
		decode[i]=(byte) ((data+127*count)/_key[pos]);
		pos++;
	}


	
	return decode;	
}
public static String dublicateChar(String _Str, char _C)
{
 int pos=0;
 
	if (_Str==null) return null;

	try
	{
		while (pos<_Str.length())
		{
			pos=_Str.indexOf(_C, pos);
			if (pos<0) break;
			_Str=_Str.substring(0, pos)+"'"+_Str.substring(pos);
			pos+=2;
		}
	}
	catch(Exception e)
	{
		Log.log("Util.dublicateChar() error : "+e);
	}
		

	return _Str;
}
// create String[] from _Str ("aaa"+_C+"bbb"+...)
public static String[] explode(String _Str, char _C, boolean _Trim)
{
 String str="", s0="";
 int pos1=0, pos2=0, len=0, n=0;
 String[] sa=null;

	if (_Str==null) return null;

	sa=new String[count(_Str, _C)+1];
	sa[0]="";
	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_C, pos1);
		if (pos2<0 || pos2>len) pos2=len;
		s0=_Str.substring(pos1, pos2);
		if (s0==null) s0="";
		if (_Trim) sa[n]=s0.trim();
		else sa[n]=s0;
		if (pos2==len) break;
		n++; pos1=pos2+1;
	}

	return sa;
}
// create String[] from _Str ("aaa"+_C+"bbb"+...)
public static String[] explode(String _Str, int _C, boolean _Trim)
{
 String str="", s0="";
 int pos1=0, pos2=0, len=0, n=0;
 String[] sa=null;
 
	if (_Str==null) return null;
	
	sa=new String[count(_Str, _C)+1];
	sa[0]="";
	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_C, pos1);
		if (pos2<0 || pos2>len) pos2=len;
		s0=_Str.substring(pos1, pos2);
		if (s0==null) s0="";
		if (_Trim) sa[n]=s0.trim();
		else sa[n]=s0;
		if (pos2==len) break;
		n++; pos1=pos2+1;
	}

	return sa;
}
public static String[] explode(String _Str, String _S)
{
 int n = 0;
 String[] sa = null;
 StringTokenizer st = null;

	st=new StringTokenizer(_Str, _S);
	sa=new String[st.countTokens()];

	while (st.hasMoreTokens()) sa[n++]=st.nextToken();

	return sa;
}
// create String[] from _Str ("aaa"+_S+"bbb"+...)
public static String[] explode(String _Str, String _S, boolean _Trim)
{
 String str="", s0="";
 int pos1=0, pos2=0, len=0, n=0;
 String[] sa=null;

	if (_Str==null) return null;

	sa=new String[count(_Str, _S)+1];
	sa[0]="";
	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_S, pos1);
		if (pos2<0 || pos2>len) pos2=len;
		s0=_Str.substring(pos1, pos2);
		if (s0==null) s0="";
		if (_Trim) sa[n]=s0.trim();
		else sa[n]=s0;
		if (pos2==len) break;
		n++; pos1=pos2+_S.length();
	}

	return sa;
}
/**
	Function cuts the string on parts separated by delimiter symbol and stores
	these parts in string array.
*/

// create String[] from _Str ("aaa"+_C+"bbb"+...)
public static String[] explodeWithQuotes(String _Str, char _C, boolean _Trim)
{
 String s0="";
 int n=0;
 String[] sa=null;

 final	char	QUOTE	=	'\'';
 boolean		quoteOpen	=	false;

 try
 {	
 	if (_Str==null) return null;
	
	// ---- cast commas ----
	n=1;
	quoteOpen = false;
	for(int i=0;i<_Str.length();i++)
	{
		if(_Str.charAt(i)==QUOTE) quoteOpen = !quoteOpen;
		if(_Str.charAt(i)==_C && !quoteOpen) n++;		
	}
	
	// ---- fill array ----
	sa	= 	new String[n];
	s0	=	"";
	quoteOpen = false;
	n=0;
	for(int i=0;i<_Str.length();i++)
	{
		if(_Str.charAt(i)==QUOTE) quoteOpen = !quoteOpen;
		if(_Str.charAt(i)==_C && !quoteOpen) 
		{
			sa[n++] = s0;
			s0 = "";	
		}
		else
		{
			s0+=_Str.charAt(i);
		}
		if(i==(_Str.length()-1) && n<sa.length)
		{
			sa[n] = s0;
		}
	}
 }
 catch(Exception e)
 {
	 Log.err("Error in Util::explodeWithQuotes() " + e);
 }
 return sa;	
}
// create String[] from _Str ("aaa"+_S+"bbb"+...)
public static String[] explodeWithQuotes(String _Str, String _S, boolean _Trim)
{
 String str="", s0="";
 int pos1=0, pos2=0, len=0, n=0;
 String[] sa=null;

	if (_Str==null) return null;

	sa=new String[count(_Str, _S)+1];
	sa[0]="";
	pos1=pos2=0; len=_Str.length();
	while (n<len)
	{
		pos2=_Str.indexOf(_S, pos1);
		if (pos2<0 || pos2>len) pos2=len;
		s0=_Str.substring(pos1, pos2);
		if (s0==null) s0="";
		if (_Trim) sa[n]=s0.trim();
		else sa[n]=s0;
		if (pos2==len) break;
		n++; pos1=pos2+_S.length();
	}

	return sa;
}
public static String generateID(String _Prefix, String _UserName) {
	int n = 0, len = 0, d = 0;
	String str = "", s0 = "", initials = "";
	String[] sa = null;
	char[] c = null;
	java.util.Calendar cal = java.util.Calendar.getInstance();
 
	try	{		// create initials string
		sa = explode(_UserName, ' ', true);
		initials = "XXX";
		if (sa.length == 1) 
			initials = "XX" + sa[0].substring(0, 1);
		if (sa.length == 2) 
			initials = sa[0].substring(0, 1) + "X" + sa[1].substring(0, 1);
		if (sa.length == 3) 
			initials = sa[0].substring(0, 1) + sa[1].substring(0, 1) + sa[2].substring(0, 1);
	}
	catch(Exception e0){}

	c = new char[9];
	str = "" + (new java.util.Date().getTime());
	len = str.length();
	for (n=0; n<4; n++) c[n] = str.charAt(len-2*n-1);
	for (n=4; n<9; n++) {
		d = 0;
		if (n==7) while (d<(int)'0' || d>(int)'9') d = (int)(255*Math.random());
		else while (d<(int)'A' || d>(int)'Z') d = (int)(255*Math.random());
		c[n] = (char)d;
	}
	
	s0 = new String(c, 0, 9);
	str = "" + cal.get(java.util.Calendar.YEAR);
	str = _Prefix+"-"+initials + str.substring(2) + "-" + s0;
	
//System.out.println(str);
	return str;
}
public static java.awt.Color getColorForRGB(String _RGB)
{
 int r=0, g=0, b=0;
 java.awt.Color c=null;

	try
	{
		r=(int)hexToInt(_RGB.substring(0, 2));
		g=(int)hexToInt(_RGB.substring(2, 4));
		b=(int)hexToInt(_RGB.substring(4, 6));
		c=new java.awt.Color(r, g, b);
	}
	catch(Exception e)
	{
		return java.awt.Color.black;
	}

	return c;
}
public static String getRGBForColor(java.awt.Color _C)
{
	try
	{
		return intToHex(_C.getRed())+intToHex(_C.getGreen())+intToHex(_C.getBlue());
	}
	catch(Exception e)
	{
		return "000000";
	}
}
public static long hexToInt(String _Str)	// tested for 00-FF only
{
 int pos1=0, pos2=0, len=0, n=0, k=0;
 long l=0, result=0;
 char ch=0;
 int[] na=null;

	if (_Str==null) return 0;

	_Str=_Str.toUpperCase();
	len=_Str.length(); na=new int[len];
	for (n=0; n<len; n++)
	{
		ch=_Str.charAt(n);
		if (ch>='0' && ch<='9') na[n]=(int)ch-48;
		if (ch=='A') na[n]=10;
		if (ch=='B') na[n]=11;
		if (ch=='C') na[n]=12;
		if (ch=='D') na[n]=13;
		if (ch=='E') na[n]=14;
		if (ch=='F') na[n]=15;
	}
	
	for (n=0; n<len; n++)
	{
		l=1;
		for (k=len-1; k>n; k--) l*=16;
		result+=(l*na[n]);
	}
	
	return result;
}
public static String intToHex(int _N)
{		// translate one Decimal byte to HEX (max value is 255)
 int n=0;
 char ch=0;
 int[] na=null;
 String str="", s0="";

	if (_N<0) return "000000";
	if (_N>255) _N=255;

	na=new int[2];
	na[0]=_N/16; na[1]=_N%16;
	for (n=0; n<2; n++)
	{
		s0=""+na[n];
		if (na[n]==10) s0="A";
		if (na[n]==11) s0="B";
		if (na[n]==12) s0="C";
		if (na[n]==13) s0="D";
		if (na[n]==14) s0="E";
		if (na[n]==15) s0="F";
		str+=s0;
	}

	return str;
}
// count _C in _Str
public static String left(String _Str, char _C)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_C);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(0, pos);
	}
	catch(Exception e){}

	return _Str;
}
// count _C in _Str
public static String left(String _Str, int _I)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_I);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(0, pos);
	}
	catch(Exception e){}

	return _Str;
}
// count _C in _Str
public static String left(String _Str, String _S)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_S);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(0, pos);
	}
	catch(Exception e){}

	return _Str;
}
static public void qSort(String[] _Array,int _left,int _right,boolean _forward,boolean _case) 
{
	int i,j;
	String x,w;

	i = _left;
	j = _right;
	if(_case) x = _Array[(_left+_right)/2].toUpperCase();
	else x = _Array[(_left+_right)/2];


	do 
	{
		if(_case)
		{
			if(_forward)
			{
				while (_Array[i].toUpperCase().compareTo(x)>0) i++;
				while (_Array[j].toUpperCase().compareTo(x)<0) j--;
			}
			else
			{
				while (_Array[i].toUpperCase().compareTo(x)<0) i++;
				while (_Array[j].toUpperCase().compareTo(x)>0) j--;				
			}
		}
		else
		{
			if(_forward)
			{
				while (_Array[i].compareTo(x)>0) i++;
				while (_Array[j].compareTo(x)<0) j--;
			}
			else
			{
				while (_Array[i].compareTo(x)<0) i++;
				while (_Array[j].compareTo(x)>0) j--;				
			}			
		}
	   
		if (i<=j) 
	   
		{
			w = _Array[i];
			_Array[i] = _Array[j];
			_Array[j] = w;
			i++;
			j--;  
		}
	} 
	while (i<j);

	
	if (_left<j) qSort(_Array,_left,j,_forward,_case);
	if (i<_right) qSort(_Array,i,_right,_forward,_case);
}
// count _C in _Str
public static String right(String _Str, char _C)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_C);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(pos+1);
	}
	catch(Exception e){}

	return _Str;
}
// count _C in _Str
public static String right(String _Str, int _I)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_I);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(pos+1);
	}
	catch(Exception e){}

	return _Str;
}
// count _C in _Str
public static String right(String _Str, String _S)
{
 int pos=0;

	try
	{
		pos=_Str.indexOf(_S);
		if (pos<0 || pos>=_Str.length()) return _Str;
		return _Str.substring(pos+_S.length());
	}
	catch(Exception e){}

	return _Str;
}
static public String[] sort(String[] _Array)
{
	qSort(_Array,0,_Array.length-1,false,false);
	return _Array;
}
static public String[] sort(String[] _Array,boolean _forward,boolean _case)
{
	qSort(_Array,0,_Array.length-1,_forward,_case);
	return _Array;
}
// make true case String from _Str ("Abcde...")
public static String toTrueCase(String _Str)
{
 String str="", s0="", s1="";

	if (_Str==null) return "";
	if (_Str.length()<1) return _Str;

	s0=_Str.substring(0, 1);
	s0=s0.toUpperCase();
	s1=_Str.substring(1, _Str.length());
	s1=s1.toLowerCase();
	str=s0+s1;

	return str;
}
public static String trim(String str)
{
	if(str!=null && str.length()>0) str=str.trim();
	else str="";

	return str;
}
}