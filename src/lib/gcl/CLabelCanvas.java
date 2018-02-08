package lib.gcl;

import java.awt.*;

public class CLabelCanvas extends CCanvas
{
 public static final int LEFT=0;
 public static final int CENTER=1;
 public static final int RIGHT=2;
 
 protected int Alignment=LEFT;
public CLabelCanvas(Container _Owner)
{
	super(_Owner);
}
public void setAlignment(int _Alignment)
{
	if (Alignment==_Alignment) return;
	
	switch (_Alignment)
	{
	  case LEFT:
	  case CENTER:
	  case RIGHT:
 	   Alignment=_Alignment;
 	   paint();
	}
}
}