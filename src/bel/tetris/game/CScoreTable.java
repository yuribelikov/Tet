package bel.tetris.game;

public class CScoreTable implements java.io.Serializable
{
  public int size = 10;
  public String[] playerNames = null;
  public int[] scores = null;

  public CScoreTable()
  {
    playerNames = new String[size];
    scores = new int[size];

    for (int n = 0; n < size; n++)
    {
      playerNames[n] = "����� " + (n + 1);
      scores[n] = 10000 * (size - n);
    }
  }

  public void newRecord(String _PlayerName, int _Score)
  {
    try
    {
      int n = 0;
      while (n < size && _Score < scores[n]) n++;

      if (n >= size) return;

      String[] playerNames = new String[size];
      int[] scores = new int[size];
      System.arraycopy(this.playerNames, 0, playerNames, 0, size);
      System.arraycopy(playerNames, n, this.playerNames, n + 1, size - n - 1);
      System.arraycopy(this.scores, 0, scores, 0, size);
      System.arraycopy(scores, n, this.scores, n + 1, size - n - 1);

      this.playerNames[n] = _PlayerName;
      this.scores[n] = _Score;
    }
    catch (Exception e)
    {
      lib.util.Log.err(getClass().getName() + ".newRecord() error : " + e);
    }
  }
}
