package lib.net;

public class CMessage implements java.io.Serializable
{
 public int Command=0;
 public Object Data=null;
public String toString()
{
	return "<"+Command+ "> <"+ Data+">";
}
}