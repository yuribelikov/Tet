package lib.util;

import java.util.*;
import java.io.*;

public class C2xVector implements Serializable
{
 private Vector[] V=null;
public C2xVector()
{
	V=new Vector[2];
	V[0]=new Vector(); V[1]=new Vector();
}
public C2xVector(int _InitialSize)
{
	V=new Vector[2];
	V[0]=new Vector(_InitialSize); V[1]=new Vector(_InitialSize);
}
public void addElement(Object[] _OA)
{
	V[0].addElement(_OA[0]); V[1].addElement(_OA[1]);
}
public void addElement(Object _O1, Object _O2)
{
	V[0].addElement(_O1); V[1].addElement(_O2);
}
public void addElements(Object[] _OA1, Object[] _OA2)
{
 int n=0;

	try
	{
		for (n=0; n<_OA1.length; n++)
		{
			V[0].addElement(_OA1[n]); V[1].addElement(_OA2[n]);
		}
	}
	catch(Exception e){}
}
// try to find _O in V[0] and return V[0].element or V[1].element
public Object element(Object _O, int _Index)
{
	try
	{
		return elementAt(V[0].indexOf(_O), _Index);
	}
	catch(Exception e)
	{
		return null;
	}
}
// try to find _O in V[1] and return V[0].element or V[1].element
public Object element2(Object _O, int _Index)
{
	try
	{
		return elementAt(V[1].indexOf(_O), _Index);
	}
	catch(Exception e)
	{
		return null;
	}
}
public Object[] elementAt(int _Pos)
{
 Object[] oa=null;
 
	try
	{
		oa=new Object[2];
		oa[0]=V[0].elementAt(_Pos); oa[1]=V[1].elementAt(_Pos);
		return oa;
	}
	catch(Exception e)
	{
		return null;
	}
}
public Object elementAt(int _Pos, int _Index)
{
	try
	{
		return V[_Index].elementAt(_Pos);
	}
	catch(Exception e)
	{
		return null;
	}
}
public int indexOf(Object _O, int _N)
{
	try
	{
		return V[_N].indexOf(_O);
	}
	catch(Exception e)
	{
		return -1;
	}
}
public void insertElementAt(Object[] _OA, int _Pos)
{
	V[0].insertElementAt(_OA[0], _Pos); V[1].insertElementAt(_OA[1], _Pos);
}
public void removeAllElements()
{
	try
	{
		V[0].removeAllElements(); V[1].removeAllElements();
		V[0].trimToSize(); V[1].trimToSize();
	}
	catch(Exception e){}
}
public void removeElement(Object _O)
{
	removeElement(_O, 0);
}
public void removeElement(Object _O, int _Index)
{
	try
	{
		removeElementAt(V[_Index].indexOf(_O));
	}
	catch(Exception e){}
}
public void removeElementAt(int _N)
{
	try
	{
		V[0].removeElementAt(_N); V[1].removeElementAt(_N);
		V[0].trimToSize(); V[1].trimToSize();
	}
	catch(Exception e){}
}
public void setElementAt(int _Pos, Object _O1, Object _O2)
{
	V[0].setElementAt(_O1, _Pos); V[1].setElementAt(_O2, _Pos);
}
public int size()
{
	try
	{
		return V[0].size();
	}
	catch(Exception e)
	{
		return 0;
	}
}
// try to find _O in V[0] and return V[0].element or V[1].element
public String string(Object _O, int _Index)
{
	try
	{
		return stringAt(V[0].indexOf(_O), _Index);
	}
	catch(Exception e)
	{
		return "";
	}
}
public String stringAt(int _Pos, int _Index)
{
	try
	{
		return (String)elementAt(_Pos, _Index);
	}
	catch(Exception e)
	{
		return "";
	}
}
public String toString()
{
 int n=0;
 String str="";
 
	try
	{
		str="2xVector ["+size()+"] :\n";
		for (n=0; n<size(); n++) str+=elementAt(n, 0)+" - "+elementAt(n, 1)+'\n';

		return str;
	}
	catch(Exception e)
	{
		return "";
	}
}
public void unique(int _Index)
{
 int n=0, k=0;
 Object o=null;
 
	try
	{
		for (n=0; n<size(); n++)
		{
			o=elementAt(n, _Index);
			for (k=n+1; k<size(); k++)
				if (o.equals(elementAt(k, _Index))) removeElementAt(k);
		}
	}
	catch(Exception e){}
}
}