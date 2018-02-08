package bel.tetris.dialog;

import bel.tetris.container.ImageContainer;
import bel.tetris.game.CScoreTable;
import lib.gcl.CGButton;
import lib.util.Log;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class CScoresBox extends CBox
{
 private String ScoresFileName=null;
 private CScoreTable ScoreTable=null;
 private Image BgImage=null;
 private Font ScoreFont=null;
public CScoresBox(String _ScoresFileName)
{
	super();

	ScoresFileName=_ScoresFileName;
	create();
}
protected void create()
{
 	try
 	{
		W=500; H=400;
	 	super.create();
		setTitle("��� �����");
		ScoreFont=new Font("Dialog", 1, 20);
			
		OkButton=new CGButton(this, ImageContainer.getImage("okButton.gif"), "������� "); add(OkButton);
		OkButton.setName("OkButton"); OkButton.setActionListener((ActionListener)EventListener);
		OkButton.setBounds(W-40, H-40, 30, 30); OkButton.setBorder(false); OkButton.setPopUpMode(true);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".create() error : "+e);
	}
}
public void paint(Graphics _G)
{
 	try
 	{
		int w=W-20, h=H-80;
		if (BgImage==null) BgImage=createImage(w, h);

		Graphics g=BgImage.getGraphics();
	 	g.setColor(Color.black);
	 	g.fillRect(0, 0, w, h);

	 	g.setFont(ScoreFont);
	 	for (int n=0; n<ScoreTable.Q; n++)
	 	{
		 	g.setColor(new Color(255-10*n, 0, 255-10*n));
		 	g.setClip(0, 0, 2*w/3-20, h);
		 	g.drawString(ScoreTable.PlayerNames[n], 20, 30+30*n);
		 	g.setColor(new Color(0, 255-10*n, 0));
		 	g.setClip(2*w/3, 0, w/3, h);
		 	g.drawString(""+ScoreTable.Scores[n], 2*w/3, 30+30*n);
	 	}

		_G.drawImage(BgImage, 10, 30, this);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".paint() error : "+e);
	}
}
public void showBox()
{
	try
	{
		OkButton.setState(1);

		File f=new File(ScoresFileName);
		if (f.exists())
		{
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
			ScoreTable=(CScoreTable)ois.readObject();
			ois.close();
		}
		else ScoreTable=new CScoreTable();

		showSelf();
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".updateScores() error : "+e);
	}
}
}
