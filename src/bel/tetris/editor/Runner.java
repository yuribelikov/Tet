package bel.tetris.editor;

import bel.tetris.container.ImageContainer;
import bel.tetris.event.CEvent;
import bel.tetris.event.CEventListener;
import bel.tetris.event.IEventReceiver;
import lib.util.Log;

import java.awt.*;
import java.awt.event.*;

public class Runner extends Frame implements IEventReceiver
{
 private CCup Cup=null;

 private CEventListener EventListener=null;
 private MenuBar MB=null;
 private Menu FileMenu=null, SelectMenu=null;
 private MenuItem SaveMI=null, ClearMI=null, QuitMI=null;

public void destroy()
{
	try
	{
		Cup.setStop();
		dispose();
//		System.runFinalization(); System.gc();
		System.exit(0);
	}
	catch(Exception e)
	{
		Log.sop(getClass().getName()+".destroy() error : "+e);
	}
}
public void dispatchEvent(CEvent _Evt)
{
	try
	{
//Log.sop(_Evt);
// - - - - - - - - mouse events - - - - - - -
		if (_Evt.getName().equals("MousePressed"))
		{
			Cup.processAction("MousePressed", _Evt);
		}
		else
// - - - - - - - - menu and window actions - - - - - - - 
		if (_Evt.getName().equals("ActionPerformed") &&
				_Evt.getSourceName().startsWith("Level"))
		{
			Cup.processAction(_Evt.getSourceName(), null);
			SaveMI.setEnabled(true); ClearMI.setEnabled(true);
		}
		else
		if (_Evt.getName().equals("ActionPerformed") &&
				_Evt.getSourceName().equals("SaveLevel"))
		{
			Cup.processAction("SaveLevel", null);
		}
		else
		if (_Evt.getName().equals("ActionPerformed") &&
				_Evt.getSourceName().equals("ClearLevel"))
		{
			Cup.processAction("ClearLevel", null);
		}
		else
		if ( (_Evt.getName().equals("ActionPerformed") &&
				 _Evt.getSourceName().equals("Quit")) ||
				 (_Evt.getName().equals("WindowClosing")) )
		{
			destroy();
		}
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".dispatchEvent() error : "+e);
	}
}
private void init(String _Args[])
{
	try
	{
//Log.LogEnabled=true;
		setBackground(new Color(210, 210, 210));
		setTitle("�������� �������"); setLayout(null); setResizable(false);
		ImageContainer.load(this);
		setIconImage(ImageContainer.getImage("icon.gif"));
		EventListener=new CEventListener(this);

		addKeyListener((KeyListener)EventListener);
		addWindowListener((WindowListener)EventListener);
		addComponentListener((ComponentListener)EventListener);
		addMouseListener((MouseListener)EventListener);
		
		Font f=new Font("Dialog", 0, 12);
		MB=new MenuBar(); setMenuBar(MB);
		FileMenu=new Menu("����"); MB.add(FileMenu); FileMenu.setFont(f);
		
		SelectMenu=new Menu("����� ������");
		FileMenu.add(SelectMenu); SelectMenu.setFont(f);

		for (int n=0; n<10; n++)
		{
			Menu menu=new Menu("  "+n+"..  "); SelectMenu.add(menu); menu.setFont(f);
			for (int k=0; k<10; k++)
			{
				if (n==0 && k==0) continue;
				MenuItem mi=new MenuItem(k+""); mi.setName("Level "+n+""+k); menu.add(mi);
				mi.setFont(f);
				mi.addActionListener((ActionListener)EventListener);
			}
		}
		
		SaveMI=new MenuItem("���������"); SaveMI.setName("SaveLevel");
		FileMenu.add(SaveMI); SaveMI.setFont(f); SaveMI.setEnabled(false);
		SaveMI.addActionListener((ActionListener)EventListener);
		FileMenu.addSeparator();
		
		ClearMI=new MenuItem("��������"); ClearMI.setName("ClearLevel");
		FileMenu.add(ClearMI); ClearMI.setFont(f); ClearMI.setEnabled(false);
		ClearMI.addActionListener((ActionListener)EventListener);
		FileMenu.addSeparator();
		
		QuitMI=new MenuItem("�����"); QuitMI.setName("Quit");
		FileMenu.add(QuitMI); QuitMI.setFont(f);
		QuitMI.addActionListener((ActionListener)EventListener);

		Cup=new CCup(this);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".init() error : "+e);
	}
}
public static void main(String[] args)
{
 Runner Application=null;

	try
	{
		Application=new Runner();
		Application.init(args);
		Application.start();
	}
	catch(Exception e)
	{
		Application.destroy();
	}
}
public void paint(Graphics g)
{
	if (Cup!=null) Cup.paint();
}
private void showSelf()
{
 Dimension d=getToolkit().getScreenSize();
 int x=0, y=0, w=0, h=0;

	w=500; h=500;
	x=d.width/2-w/2; y=d.height/2-h/2;// y=0;
	if (x<0) x=0; if (y<0) y=0;
	setBounds(x, y, w, h);
	show();
}
private void start()
{
	try
	{
		showSelf();

		Cup.start();
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".start() error : "+e);
		destroy();
	}
}
}
