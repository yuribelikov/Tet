package bel.tetris.game;

import java.awt.*;
import java.io.*;
import lib.util.*;

public class CCup extends Thread
{
 private Runner Runner=null;

 private int W=10, H=20, F=4;		// cup width and height, figure width/height
 private int[][] Contents=null;
 private String FigureTypes[]={"LStair", "RStair", "Podium", "LCorner", "RCorner", "Square", "Line"};
 private CFigure CurrentFigure=null, NextFigure=null;
 private int[] PrevFigureTypes=null;
 private int PlayerN=1, Speed=0, Level=0, Score=0, Prize=0;
 private boolean IsNextFigureShowed=false;
 private String PlayerName=null;

 private int SquareSize=20;
 private Image BgImage=null;
 private Point StartLocation=null;
 private Color[] Palette={null, new Color(222, 222, 222), Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.orange, Color.pink};
 private Color CupColor=null, CurrentFigureColor=null, NextFigureColor=null,
 	MergeColor=null, ScoreColor=null, MessageColor=null, InfoColor=null, PlayerNameColor=null;
 private Font ScoreFont=null, MessageFont=null, InfoFont=null, PlayerNameFont=null;
 private String Text=null;

 private String State=null, PrevState=null;
 private int DropDelay=10, MergeDelay=500;
 private boolean IsAlive=true, IsShouldBeRepainted=false;
public CCup(Runner _Runner, int _PlayerN)
{
	super();

	Runner=_Runner;
	PlayerN=_PlayerN;
	PrevFigureTypes=new int[2];
	StartLocation=new Point(3, -2);
	setState("Idle");

	CupColor=new Color(150, 150, 255);
	CurrentFigureColor=new Color(180, 255, 180);
	NextFigureColor=new Color(255, 100, 100);
	MergeColor=new Color(255, 150, 255);
	ScoreColor=new Color(220, 220, 255);
	ScoreFont=new Font("Dialog", 1, 24);
	MessageColor=new Color(255, 255, 150);
	MessageFont=new Font("Dialog", 1, 60);
	InfoColor=new Color(220, 220, 240);
	InfoFont=new Font("Dialog", 1, 48);
	PlayerNameColor=new Color(240, 240, 255);
	PlayerNameFont=new Font("Dialog", 1, 32);
}
private boolean cfIsValid()
{
	try
	{
		for (int y=0; y<CurrentFigure.getContents().length; y++)
		for (int x=0; x<CurrentFigure.getContents()[0].length; x++)
		{
			if (!CurrentFigure.getContents()[y][x]) continue;
			int cx=CurrentFigure.Location.x+x; int cy=CurrentFigure.Location.y+y;
			if (cx<0 || cx>=W) return false;
			if (cy>=H) return false;
			if (cy<0) continue;
			if (Contents[cy][cx]>0) return false;
		}

		return true;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".cfIsValid() error : "+e);
	}

	return false;
}
private void cfMerge()
{
	try
	{
		for (int y=0; y<CurrentFigure.getContents().length; y++)
		for (int x=0; x<CurrentFigure.getContents()[0].length; x++)
		{
			if (!CurrentFigure.getContents()[y][x]) continue;
			int cx=CurrentFigure.Location.x+x; int cy=CurrentFigure.Location.y+y;
			if (cx<0 || cx>=W) continue;
			if (cy<0 || cy>=H) continue;
			Contents[cy][cx]=1;
		}

		int completedRowsQ=0;
		boolean[] completedRows=new boolean[H];
		for (int y=0; y<H; y++)
		{
			boolean isRowComplete=true;
			for (int x=0; x<W; x++)
			{
				if (Contents[y][x]==0)
				{
					isRowComplete=false; break;
				}
			}
			completedRows[y]=isRowComplete;
			if (isRowComplete) completedRowsQ++;
		}

		if (completedRowsQ>0)
		{
			Prize=100*completedRowsQ*completedRowsQ*Level*(Speed+1);
			paint(); sleep(MergeDelay);
			Score+=Prize; Prize=0;
			for (int y=0; y<H; y++)
			{
				if (completedRows[y])
				{
					int[][] contents=new int[H][W];
					System.arraycopy(Contents, 0, contents, 1, y);
					System.arraycopy(Contents, y+1, contents, y+1, H-y-1);
					Contents=contents;
					for (int x=0; x<W; x++) Contents[0][x]=0;
				}	
			}
		}

		paint();
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".cfMerge() error : "+e);
	}
}
private void cfNext()
{
	CurrentFigure=NextFigure;
	CurrentFigure.Location.x=StartLocation.x;
	CurrentFigure.Location.y=StartLocation.y;
	NextFigure=generateFigure();
}
public void finishGame()
{
	Runner.updateScores(PlayerName, Score, false);
}
private CFigure generateFigure()
{
	try
	{
		int type=-1;
		while (type<0 || (type==PrevFigureTypes[0] && type==PrevFigureTypes[1]))
			type=(int)Math.round((FigureTypes.length-1)*Math.random());

		PrevFigureTypes[0]=PrevFigureTypes[1]; PrevFigureTypes[1]=type;
		CFigure figure=new CFigure();
		figure.Type=FigureTypes[type];
//figure.Type="Line";
		figure.Location=new Point();
		figure.Contents=new boolean[4][F][F];

		if (figure.Type.equals("LStair"))
		{
			figure.Contents[0][1][2]=figure.Contents[0][1][3]=								//  xx
			figure.Contents[0][2][1]=figure.Contents[0][2][2]=true;						// xx

			figure.Contents[1][1][1]=																					// x
			figure.Contents[1][2][1]=figure.Contents[1][2][2]=								// xx
			figure.Contents[1][3][2]=true;																		//  x

			figure.Contents[2][1][2]=figure.Contents[2][1][3]=								//  xx
			figure.Contents[2][2][1]=figure.Contents[2][2][2]=true;						// xx

			figure.Contents[3][1][1]=																					// x
			figure.Contents[3][2][1]=figure.Contents[3][2][2]=								// xx
			figure.Contents[3][3][2]=true;																		//  x
		}
		else
		if (figure.Type.equals("RStair"))
		{
			figure.Contents[0][1][0]=figure.Contents[0][1][1]=								// xx
			figure.Contents[0][2][1]=figure.Contents[0][2][2]=true;						//  xx

			figure.Contents[1][1][2]=																					//  x
			figure.Contents[1][2][1]=figure.Contents[1][2][2]=								// xx
			figure.Contents[1][3][1]=true;																		// x

			figure.Contents[2][1][0]=figure.Contents[2][1][1]=								// xx
			figure.Contents[2][2][1]=figure.Contents[2][2][2]=true;						//  xx

			figure.Contents[3][1][2]=																					//  x
			figure.Contents[3][2][1]=figure.Contents[3][2][2]=								// xx
			figure.Contents[3][3][1]=true;																		// x
		}

		else
		if (figure.Type.equals("Podium"))
		{
			figure.Contents[0][1][2]=																					//   x
			figure.Contents[0][2][1]=figure.Contents[0][2][2]=								//  xx
			figure.Contents[0][3][2]=true;																		//   x

			figure.Contents[1][1][1]=																															//  x
			figure.Contents[1][2][0]=figure.Contents[1][2][1]=figure.Contents[1][2][2]=true;			// xxx

			figure.Contents[2][1][1]=																					//  x
			figure.Contents[2][2][1]=figure.Contents[2][2][2]=								//  xx
			figure.Contents[2][3][1]=true;																		//  x

			figure.Contents[3][1][0]=figure.Contents[3][1][1]=figure.Contents[3][1][2]=						// xxx
			figure.Contents[3][2][1]=true;																												//  x
		}

		else
		if (figure.Type.equals("LCorner"))
		{
			figure.Contents[0][1][1]=figure.Contents[0][1][2]=								//  xx
			figure.Contents[0][2][2]=																					//   x
			figure.Contents[0][3][2]=true;																		//   x

			figure.Contents[1][1][2]=																															//   x
			figure.Contents[1][2][0]=figure.Contents[1][2][1]=figure.Contents[1][2][2]=true;			// xxx

			figure.Contents[2][1][1]=																					//  x
			figure.Contents[2][2][1]=																					//  x
			figure.Contents[2][3][1]=figure.Contents[2][3][2]=true;						//  xx

			figure.Contents[3][1][0]=figure.Contents[3][1][1]=figure.Contents[3][1][2]=						// xxx
			figure.Contents[3][2][0]=true;																												// x
		}
		else
		if (figure.Type.equals("RCorner"))
		{
			figure.Contents[0][1][1]=figure.Contents[0][1][2]=								//  xx
			figure.Contents[0][2][1]=																					//  x
			figure.Contents[0][3][1]=true;																		//  x

			figure.Contents[1][1][0]=figure.Contents[1][1][1]=figure.Contents[1][1][2]=						// xxx
			figure.Contents[1][2][2]=true;																												//   x

			figure.Contents[2][1][2]=																					//   x
			figure.Contents[2][2][2]=																					//   x
			figure.Contents[2][3][1]=figure.Contents[2][3][2]=true;						//  xx

			figure.Contents[3][1][0]=																															// x
			figure.Contents[3][2][0]=figure.Contents[3][2][1]=figure.Contents[3][2][2]=true;			// xxx
		}

		else
		if (figure.Type.equals("Square"))
		{
			for (int n=0; n<4; n++)
			{
				figure.Contents[n][1][1]=figure.Contents[n][1][2]=																			//  xx
				figure.Contents[n][2][1]=figure.Contents[n][2][2]=true;																	//  xx
			}
		}

		else
		if (figure.Type.equals("Line"))
		{
			figure.Contents[0][2][0]=figure.Contents[0][2][1]=figure.Contents[0][2][2]=figure.Contents[0][2][3]=true;		//  xxxx
			
			figure.Contents[1][0][2]=																														//  x
			figure.Contents[1][1][2]=																														//  x
			figure.Contents[1][2][2]=																														//  x
			figure.Contents[1][3][2]=true;																											//  x
			
			figure.Contents[2][2][0]=figure.Contents[2][2][1]=figure.Contents[2][2][2]=figure.Contents[2][2][3]=true;		//  xxxx
			
			figure.Contents[3][0][2]=																														//  x
			figure.Contents[3][1][2]=																														//  x
			figure.Contents[3][2][2]=																														//  x
			figure.Contents[3][3][2]=true;																											//  x
		}

Log.log(figure);
		return figure;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".generateFigure() error : "+e);
	}

	return null;
}
private boolean isLevelComplete()
{
	try
	{
		for (int y=0; y<H; y++)
		for (int x=0; x<W; x++)
			if (Contents[y][x]>1) return false;

		return true;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".isLevelComplete() error : "+e);
	}

	return false;
}
private boolean loadLevel()
{
	try
	{
		Contents=new int[H][W];
		File f=new File(Level+".lvl");
		if (!f.exists()) return false;

		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
		Contents=(int[][])ois.readObject();
		ois.close();

		return true;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".loadLevel() error : "+e);
	}
	return false;
}
public void newGame(java.util.Hashtable _Data)
{
	try
	{
		Contents=new int[H][W];
		CurrentFigure=NextFigure=null;
		Level=1; Score=0;
		setState("Idle");

		PlayerName=(String)_Data.get("Player"+PlayerN+"Name");
		Speed=(int)Integer.parseInt((String)_Data.get("Speed"));
		IsNextFigureShowed=_Data.get("IsNextFigureShowed").equals("true");
		loadLevel();
		NextFigure=generateFigure();
		cfNext();

		setState("Game");
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".newGame() error : "+e);
	}
}
public synchronized void paint()
{
	try
	{
		if (Runner.getPlayersQ()==1 && PlayerN==2) return;

		Dimension scrSize=Runner.getSize();

		Graphics g=BgImage.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, scrSize.width/2, scrSize.height);

		if (!State.equals("Idle"))
		{
			g.setColor(CupColor);
			int w=SquareSize*W; int h=SquareSize*H;
			int x=scrSize.width/4-w/2; int y=scrSize.height/2-h/2+30;
			g.fillRect(x-4, y, 4, h+4);
			g.fillRect(x+w, y, 4, h+4);
			g.fillRect(x, y+h, w, 4);

			if (IsNextFigureShowed)
				paintFigure(NextFigure, g, NextFigureColor, x+w+10, y+h/2-SquareSize*2);
			paintContents(g, x, y);
			paintFigure(CurrentFigure, g, CurrentFigureColor, x, y);
			
			paintText(Text, g, MessageFont, MessageColor, x+w/2, y+80);
			paintText(PlayerName, g, PlayerNameFont, PlayerNameColor, x+w/2, y-65);
			paintText(""+Level, g, InfoFont, InfoColor, x+w+50, y+20);
			paintText(Prize==0?(""+Score):(Score+" + "+Prize), g, ScoreFont, ScoreColor, x+w/2, y+h);
			int mx=x+w/2, my=y+h/2-50;
			if (Prize>0) paintText(""+Prize, g, MessageFont, MessageColor, mx, my);
			if (State.equals("Pause")) paintText("œ¿”«¿", g, MessageFont, MessageColor, mx, my);
			else
			if (State.equals("Over"))
			{
				paintText("¬€", g, MessageFont, MessageColor, mx, my);
				paintText("œ–Œ»√–¿À»", g, MessageFont, MessageColor, mx, my+60);
			}
			else
			if (State.equals("Victory"))
			{
				paintText("¬€", g, MessageFont, MessageColor, mx, my);
				paintText("œŒ¡≈ƒ»À»", g, MessageFont, MessageColor, mx, my+60);
			}
		}

		g=Runner.getGraphics();
		int x=0;
		if (Runner.getPlayersQ()==1) x=scrSize.width/4;
		else x=(PlayerN-1)*scrSize.width/2;
		synchronized (g)
		{
			g.drawImage(BgImage, x, 0, Runner);
		}
		IsShouldBeRepainted=false;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".paint() error : "+e);
	}
}
private void paintContents(Graphics _G, int _X, int _Y)
{
	try
	{
		if (Contents==null) return;

		for (int y=0; y<H; y++)
		{
			boolean isRowComplete=true;
			for (int x=0; x<W; x++)
			{
				if (Contents[y][x]==0)
				{
					isRowComplete=false; break;
				}
			}
			for (int x=0; x<W; x++)
			{
				if (Contents[y][x]>0)
				{
					if (isRowComplete) _G.setColor(MergeColor);
					else _G.setColor(Palette[Contents[y][x]]);
					_G.fillRect(_X+x*SquareSize,
											_Y+y*SquareSize,
											SquareSize, SquareSize);
				}
			}
		}
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".paintContents() error : "+e);
	}
}
private void paintFigure(CFigure _Figure, Graphics _G, Color _C, int _X, int _Y)
{
	try
	{
		if (_Figure==null) return;

		_G.setColor(_C);
		for (int y=0; y<_Figure.getContents().length; y++)
		for (int x=0; x<_Figure.getContents()[0].length; x++)
		{
			if (_Figure.getContents()[y][x])
			{
				_G.fillRect(_X+(_Figure.Location.x+x)*SquareSize,
										_Y+(_Figure.Location.y+y)*SquareSize,
										SquareSize, SquareSize);
			}
		}
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".paintFigure() error : "+e);
	}
}
private void paintText(String _Text, Graphics _G, Font _Font, Color _Color, int _X, int _Y)
{
	try
	{
		if (_Text==null) return;

		_G.setFont(_Font); _G.setColor(_Color);
		int sw=_G.getFontMetrics().stringWidth(_Text);
		int sh=_G.getFontMetrics().getHeight();
		int sx=_X-sw/2; int sy=_Y+sh+5;
		_G.drawString(_Text, sx, sy);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".paintText() error : "+e);
	}
}
public void processAction(String _Action)
{
	try
	{
		if (_Action.equals("No"))
		{
			State=PrevState;
		}
		else
		if (_Action.equals("NewGame"))
		{
			Runner.newGame();
		}
		else
		if (_Action.equals("Pause") && State.equals("Game"))
		{
			setState("Pause");
		}
		else
		if (_Action.equals("Pause") && State.equals("Pause"))
		{
			setState("Game");
		}
		else
		if (_Action.equals("Quit") && State.equals("Game"))
		{
			setState("Pause");
		}
		else
		if (_Action.equals("Quit") && !State.equals("Game"))
		{
			setState(State);
		}
		else
		if (_Action.equals("MoveLeft") && State.equals("Game"))
		{
			CurrentFigure.Location.x--;
			if (!cfIsValid()) CurrentFigure.Location.x++;
			else IsShouldBeRepainted=true;
		}
		else
		if (_Action.equals("MoveRight") && State.equals("Game"))
		{
			CurrentFigure.Location.x++;
			if (!cfIsValid()) CurrentFigure.Location.x--;
			else IsShouldBeRepainted=true;
		}
		else
		if (_Action.equals("Rotate") && State.equals("Game"))
		{
			CurrentFigure.rotate();
			if (!cfIsValid()) CurrentFigure.rotateBack();
			else IsShouldBeRepainted=true;
		}
		else
		if (_Action.equals("Drop") && State.equals("Game"))
		{
			setState("Drop");
		}

	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".processAction() error : "+e);
	}
}
public void run()
{
Log.log(getClass().getName()+" is started.");
	try
	{
		while (BgImage==null)
		{
			sleep(100);
			BgImage=Runner.createImage(Runner.getSize().width/2, Runner.getSize().height);
		}

		while (IsAlive)
		{
			if (!(State.equals("Game") || State.equals("Drop")))
			{
				paint(); sleep(50); continue;
			}

			if (!cfIsValid())
			{
				CurrentFigure.Location.y--;
				cfMerge();
				if (isLevelComplete())
				{
					CurrentFigure=null;
					paint(); sleep(500);
					Prize=5000*Level*(Speed+1); Text="œËÁ";
					paint(); sleep(3000);
					Score+=Prize; Prize=0; Contents=null; Text=null;
					paint(); sleep(500);
					Level++;
					if (loadLevel())
					{
						Text="ÛÓ‚ÂÌ¸ "+Level;
					}
					else
					{
						setState("Victory");
						Runner.updateScores(PlayerName, Score, true);
						continue;
					}
					paint(); sleep(2000); Text=null;
				}
				cfNext();
				if (!cfIsValid())
				{
					setState("Over"); paint(); sleep(2000);
					Runner.updateScores(PlayerName, Score, true);
				}
				else setState("Game");
			}

			paint();

			if (State.equals("Drop")) sleep(DropDelay);
			else
			{
				int delay=0;
				switch (Speed)
				{
					case 0 : delay=1500; break;
					case 1 : delay=900; break;
					case 2 : delay=500; break;
					case 3 : delay=200; break;
					case 4 : delay=80; break;
				}
				for (int n=0; n<delay; n++)
				{
					if (!State.equals("Game")) break;
					sleep(1);
					if (IsShouldBeRepainted) paint();
				}
			}

			CurrentFigure.Location.y++;
		}
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".run() error : "+e);
	}
Log.log(getClass().getName()+" is stopped.");
}
private void setState(String _NewState)
{
	PrevState=State; State=_NewState;
	IsShouldBeRepainted=true;
}
public void setStop()
{
	IsAlive=false;
}
}
