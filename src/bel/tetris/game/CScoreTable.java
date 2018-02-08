package bel.tetris.game;

public class CScoreTable implements java.io.Serializable
{
 public int Q=10;
 public String[] PlayerNames=null;
 public int[] Scores=null;
public CScoreTable()
{
	PlayerNames=new String[Q];
	Scores=new int[Q];

	for (int n=0; n<Q; n++)
	{
		PlayerNames[n]="Игрок "+(n+1);
		Scores[n]=10000*(Q-n);
	}
}
public void newRecord(String _PlayerName, int _Score)
{
	try
	{
		int n=0;
		while (n<Q && _Score<Scores[n]) n++;

		if (n>=Q) return;

		String[] playerNames=new String[Q];
		int[] scores=new int[Q];
		System.arraycopy(PlayerNames, 0, playerNames, 0, Q);
		System.arraycopy(playerNames, n, PlayerNames, n+1, Q-n-1);
		System.arraycopy(Scores, 0, scores, 0, Q);
		System.arraycopy(scores, n, Scores, n+1, Q-n-1);

		PlayerNames[n]=_PlayerName; Scores[n]=_Score;
	}
	catch(Exception e)
	{
		lib.util.Log.err(getClass().getName()+".newRecord() error : "+e);
	}
}
}
