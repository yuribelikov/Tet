package bel.tetris.game;

import java.awt.*;

public class CFigure
{
 protected String Type=null;
 protected boolean[][][] Contents=null;
 protected int Rotation=0;
 protected Point Location=null;
public boolean[][] getContents()
{
	return Contents[Rotation];
}
public void rotate()
{
	Rotation--;
	if (Rotation<0) Rotation=3;
}
public void rotateBack()
{
	Rotation++;
	if (Rotation>3) Rotation=0;
}
public String toString()
{
	return "Figure : "+Type+" ["+90*Rotation+"], "+Location;
}
}
