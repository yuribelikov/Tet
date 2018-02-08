package lib.xml;

import java.util.*;
import java.io.*;

public class XMLParser
{
 public CXMLObject RootXMLObject=null;
 public String Type=null;
 public String[] TagOriginals={"\r\n", "\r", "\n"},
 		TagReplacement={" ", " ", " "},
 		BodyOriginals={"&amp;", "&gt;", "&lt;", "&quot", "&apos"},
 		BodyReplacement={"&", ">", "<", "\"", "'"};
private Vector createXMLObjects(CXMLObject _XMLObject, String _Xml) throws XMLException
{
	Vector v=new Vector();
	int mainPos=0, openTagStartPos=0, openTagEndPos=0, closeTagStartPos=0, closeTagEndPos=0;
	boolean isShort=false;

	while (true)
	{
		openTagStartPos=_Xml.indexOf('<', mainPos);		// find open tag
		if (openTagStartPos<0) break;
		openTagEndPos=_Xml.indexOf('>', openTagStartPos);
		if (_Xml.charAt(openTagStartPos+1)=='/')
		{
			throw new XMLException("unknown close tag is found: "+_Xml.substring(openTagStartPos, openTagEndPos+1), XMLException.CLOSE_TAG_BEFORE_OPEN, null);
		}
		String head=replaceSpecificSymbols(_Xml.substring(openTagStartPos+1, openTagEndPos), TagOriginals, TagReplacement);
		head=head.trim(); String name=null;
		isShort=(head.charAt(head.length()-1)=='/');
		if (isShort) head=head.substring(0, head.length()-1);
		int endNamePos=head.indexOf(' ');
		if (endNamePos<0) name=head;
		else name=head.substring(0, endNamePos);

		if (name.startsWith("!--"))
		{
			mainPos=_Xml.indexOf("-->", openTagStartPos+3);
			continue;
		}
		
		if (name.startsWith("?") || name.startsWith("!"))
		{
			if (name.equalsIgnoreCase("!DOCTYPE"))
			{
				int p1=endNamePos+1;
				while (head.charAt(p1)==' ') p1++;
				Type=head.substring(p1, head.indexOf(' ', p1));
			}
			mainPos=openTagEndPos+1;
			continue;
		}
		
		if (!isShort)
		{
			int pos=openTagEndPos, closeTags=1;
			while (true)								// find close tag
			{
				int closeNamePos=_Xml.indexOf(name, pos);
				if (closeNamePos<0)
				{
					throw new XMLException("close tag is not found for open tag: <"+name+">",
								XMLException.NO_CLOSE_TAG, null);
				}		// checking char after tag (should be space or >)
				int p = closeNamePos + name.length();
				if (p < _Xml.length())
				{
					char c = _Xml.charAt(p);
					if (c != ' ' && c != '>')
					{
						pos = closeNamePos + name.length(); continue;
					}
				}
				boolean result=true; int n=0;
				for (n=closeNamePos-1; n>openTagEndPos; n--)
				{
					char c=_Xml.charAt(n);
					if (c==' ') continue;
					if (c=='/' && _Xml.charAt(n-1)=='<')
						closeTags--;
					if (c=='<')
						closeTags++;
					if (closeTags == 0)
						break;
					result=false; break;
				}
				if (!result)
				{
					pos=closeNamePos+name.length(); continue;
				}
				closeTagStartPos=n; result=true;
				for (n=closeNamePos+name.length(); n<_Xml.length(); n++)
				{
					char c=_Xml.charAt(n);
					if (c==' ') continue;
					if (c=='>') break;
					result=false; break;
				}
				if (!result)
				{
					pos=closeNamePos+name.length(); continue;
				}
				closeTagEndPos=n; break;
			}
		}
															// create new object
		CXMLObject xmlObject=new CXMLObject(); v.addElement(xmlObject);
		xmlObject.Parent=_XMLObject;
		xmlObject.Name=name;
		xmlObject.Attributes=new Hashtable();
		if (endNamePos>0)					// parse attributes
		{
			String attribs=head.substring(name.length()+1);
			String[] sa=explode(attribs, "=");
			for (int n=0; n<sa.length-1; n++)
			{
				String key=null, value=null;
				int leftPos=sa[n].lastIndexOf(' ');
				if (leftPos<0 && n>0)
					throw new XMLException("illegal attribute: '"+sa[n]+"="+sa[n+1]+"' is found in tag: <"+head+">", XMLException.ILLEGAL_ATTRIBUTE, null);
				else key=(leftPos<0?sa[n]:sa[n].substring(leftPos+1));
				if (key.length()==0)
					throw new XMLException("illegal attribute: '"+sa[n]+"="+sa[n+1]+"' is found in tag: <"+head+">", XMLException.ILLEGAL_ATTRIBUTE, null);
				int rightOpenPos1=sa[n+1].indexOf('"'),
						rightOpenPos2=sa[n+1].indexOf('\''), rightClosePos=0;
				if (rightOpenPos1<0 && rightOpenPos2<0)
					throw new XMLException("illegal attribute: '"+sa[n]+"="+sa[n+1]+"' is found in tag: <"+head+">", XMLException.ILLEGAL_ATTRIBUTE, null);
				else
					if (rightOpenPos1>=0)
						rightClosePos=sa[n+1].indexOf('"', rightOpenPos1+1);
					else
						rightClosePos=sa[n+1].indexOf('\'', rightOpenPos2+1);
				if (rightClosePos<0)
					throw new XMLException("illegal attribute: '"+sa[n]+"="+sa[n+1]+"' is found in tag: <"+head+">", XMLException.ILLEGAL_ATTRIBUTE, null);
				int rightOpenPos=1+(rightOpenPos1>=0?rightOpenPos1:rightOpenPos2);
				value=replaceSpecificSymbols(sa[n+1].substring(rightOpenPos, rightClosePos), BodyOriginals, BodyReplacement);
				xmlObject.Attributes.put(key.toLowerCase(), value);
			}
		}

		if (isShort) mainPos=openTagEndPos+1;
		else
		{
			String body=_Xml.substring(openTagEndPos+1, closeTagStartPos-1);
			xmlObject.Children=createXMLObjects(xmlObject, body);
			if (xmlObject.Children.size()==0)		// if no children then a body may exist
				xmlObject.Body=replaceSpecificSymbols(body, BodyOriginals, BodyReplacement);
			mainPos=closeTagEndPos+1;
		}
		if (mainPos>=_Xml.length()) break;
	}

	return v;
}
private String[] explode(String _Str, String _S)
{
 int n = 0;
 String[] sa = null;
 StringTokenizer st = null;

	st=new StringTokenizer(_Str, _S);
	sa=new String[st.countTokens()];

	while (st.hasMoreTokens()) sa[n++]=st.nextToken().trim();

	return sa;
}
public CXMLObject find(String _name, int _n, CXMLObject _parent) throws Exception
{
	if (_parent==null) _parent=RootXMLObject;
	if (_parent.Children==null) return null;
	int i=0;
	for (int n=0; n<_parent.Children.size(); n++)
	{
		CXMLObject obj=(CXMLObject)_parent.Children.elementAt(n);
		if (obj.Name.equals(_name))
		{
			if (i==_n) return obj;
			else i++;
		}
		CXMLObject child=find(_name, _n, obj);
		if (child!=null) return child;
	}

	return null;
}
public int getCount(String _name, CXMLObject _parent) throws Exception
{
	if (_parent==null) _parent=RootXMLObject;
	if (_parent.Children==null) return 0;
	int i=0;
	for (int n=0; n<_parent.Children.size(); n++)
	{
		CXMLObject obj=(CXMLObject)_parent.Children.elementAt(n);
		if (obj.Name.equals(_name)) i++;
		i+=getCount(_name, obj);
	}

	return i;
}
public void parseXML(String _XmlDoc) throws Exception
{
	RootXMLObject=new CXMLObject();
	RootXMLObject.Children=createXMLObjects(RootXMLObject, _XmlDoc);
}
public String replaceSpecificSymbols(String _XmlDoc, String[] _Originals, String[] _Replacement) throws XMLException
{
	int n=0;
	StringBuffer sb=new StringBuffer(_XmlDoc.length());
	int pos=0;
	for (n=0; n<_XmlDoc.length(); n++)
	{
		if (_XmlDoc.charAt(n)>='A') continue;
		for (int k=0; k<_Originals.length; k++)
			if (_XmlDoc.charAt(n)==_Originals[k].charAt(0) &&
					_Originals[k].equalsIgnoreCase(_XmlDoc.substring(n, n+_Originals[k].length())))
			{
				sb.append(_XmlDoc.substring(pos, n));
				sb.append(_Replacement[k]);
				n+=_Originals[k].length(); pos=n;
			}
	}

	sb.append(_XmlDoc.substring(pos, _XmlDoc.length()));
	return sb.toString();
}
}
