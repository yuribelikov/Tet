package bel.tetris.game;

public class COptions implements java.io.Serializable
{
 public String[] Actions=null;
 public String[][] Keys=null;
public COptions()
{
	super();

	Actions=new String[4];
	Keys=new String[2][4];
	Actions[0]="MoveLeft";
	Actions[1]="MoveRight";
	Actions[2]="Rotate";
	Actions[3]="Drop";
	Keys[0][0]="Z";
	Keys[0][1]="X";
	Keys[0][2]="Q";
	Keys[0][3]="A";
	Keys[1][0]="Left";
	Keys[1][1]="Right";
	Keys[1][2]="Up";
	Keys[1][3]="Down";
}
public COptions(COptions _Options)
{
	super();

	Actions=new String[4];
	Keys=new String[2][4];

	for (int n=0; n<Actions.length; n++) Actions[n]=_Options.Actions[n];
	
	for (int n=0; n<Keys.length; n++)
	for (int k=0; k<Keys[0].length; k++)
		Keys[n][k]=_Options.Keys[n][k];
}
}
