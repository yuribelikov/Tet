package bel.tetris.dialog;

import bel.tetris.container.ImageContainer;
import bel.tetris.event.CEvent;
import bel.tetris.game.COptions;
import lib.gcl.CButton;
import lib.gcl.CGButton;
import lib.util.Log;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class COptionsBox extends CBox
{
 private COptions Options=null;
 private CButton[][] Buttons=null;
 private CGButton InterchangeButton=null;
public COptionsBox()
{
	super();

	create();
}
protected void create()
{
 	try
 	{
		W=300; H=220;
		super.create();

		setTitle("���������");
		
		Label l=new Label("����� 1:"); add(l);
		l.setBounds(120, 35, 80, 20);
		l.addKeyListener((KeyListener)EventListener);
		l=new Label("����� 2:"); add(l);
		l.setBounds(210, 35, 80, 20);
		l=new Label("����� :"); add(l);
		l.setBounds(10, 65, 90, 20);
		l=new Label("������ :"); add(l);
		l.setBounds(10, 90, 90, 20);
		l=new Label("������� :"); add(l);
		l.setBounds(10, 115, 90, 20);
		l=new Label("������� :"); add(l);
		l.setBounds(10, 140, 90, 20);

		Buttons=new CButton[2][4];
		for (int n=0; n<Buttons.length; n++)
		for (int k=0; k<Buttons[0].length; k++)
		{
			Buttons[n][k]=new CButton(this, "", "������� ����� �������       "); add(Buttons[n][k]);
			Buttons[n][k].setBounds(120+90*n, 64+25*k, 50, 22);
			Buttons[n][k].setActionListener((ActionListener)EventListener);
			Buttons[n][k].setForeground(Color.blue);
			Buttons[n][k].setRadioMode(true); Buttons[n][k].setBorder(false);
		}

		InterchangeButton=new CGButton(this, ImageContainer.getImage("interchange.gif"), "�������� �������      "); add(InterchangeButton);
		InterchangeButton.setName("InterchangeButton"); InterchangeButton.setActionListener((ActionListener)EventListener);
		InterchangeButton.setBounds(177, 99, 24, 24); InterchangeButton.setBorder(false); InterchangeButton.setPopUpMode(true);

		OkButton=new CGButton(this, ImageContainer.getImage("okButton.gif"), "��"); add(OkButton);
		OkButton.setName("OkButton"); OkButton.setActionListener((ActionListener)EventListener);
		OkButton.setBounds(W-75, H-40, 30, 30); OkButton.setBorder(false); OkButton.setPopUpMode(true);
		CancelButton=new CGButton(this, ImageContainer.getImage("cancelButton.gif"), "������  "); add(CancelButton);
		CancelButton.setName("CancelButton"); CancelButton.setActionListener((ActionListener)EventListener);
		CancelButton.setBounds(W-40, H-40, 30, 30); CancelButton.setBorder(false); CancelButton.setPopUpMode(true);

		requestFocus();
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".create() : "+e);
	}
}
public void dispatchEvent(CEvent _Evt)
{
	try
	{
		super.dispatchEvent(_Evt);
		
		if (_Evt.getName().equals("ActionPerformed"))
		{
			if (_Evt.getSource().equals(InterchangeButton))
			{
				String[] keys=Options.Keys[0];
				Options.Keys[0]=Options.Keys[1];
				Options.Keys[1]=keys;
				for (int n=0; n<Buttons.length; n++)
				for (int k=0; k<Buttons[0].length; k++)
					Buttons[n][k].setText(Options.Keys[n][k]);
//				String[] keys=new String[Options.Keys[0].length];
//				for (int n=0; n<keys.length; n++) keys[n]=Options.Keys[0][n];
//				for (int n=0; n<keys.length; n++) keys[n]=Options.Keys[0][n];
			}

			for (int n=0; n<Buttons.length; n++)
			for (int k=0; k<Buttons[0].length; k++)
				if (_Evt.getSource().equals(Buttons[n][k]))
				{
					if (Buttons[n][k].getState()==0)
					{
						setTitle("������� �������");
						Buttons[n][k].setText("?");
						setControlEnabled(false, Buttons[n][k]);
					}
					else
					{
						setTitle("���������");
						Buttons[n][k].setText(Options.Keys[n][k]);
						setControlEnabled(true, Buttons[n][k]);
					}
					break;
				}
		}

		if (_Evt.getName().equals("KeyPressed"))
		{
			KeyEvent keyEvt=(KeyEvent)_Evt.getData();
			String keyKode=keyEvt.getKeyText(keyEvt.getKeyCode());

			for (int n=0; n<Buttons.length; n++)
			for (int k=0; k<Buttons[0].length; k++)
				if (Buttons[n][k].getState()==1)
				{
					setTitle("���������");
					Buttons[n][k].setText(keyKode);
					Options.Keys[n][k]=keyKode;
					Buttons[n][k].setState(0);
					setControlEnabled(true, Buttons[n][k]);
					break;
				}
		}

	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".dispatchEvent() error : "+e);
	}
}
private void setControlEnabled(boolean _IsEnabled, Component _Component)
{
	try
	{
		if (!_Component.equals(OkButton)) OkButton.setEnabled(_IsEnabled);
		if (!_Component.equals(CancelButton)) CancelButton.setEnabled(_IsEnabled);
		if (!_Component.equals(InterchangeButton)) InterchangeButton.setEnabled(_IsEnabled);
		for (int n=0; n<Buttons.length; n++)
		for (int k=0; k<Buttons[0].length; k++)
			if (!_Component.equals(Buttons[n][k])) Buttons[n][k].setEnabled(_IsEnabled);
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".setControlEnabled() error : "+e);
	}
}
public COptions showBox(COptions _Options)
{
	try
	{
		Result=false; Options=new COptions(_Options);
		setControlEnabled(true, this);
		OkButton.setState(1); CancelButton.setState(1);
		for (int n=0; n<Buttons.length; n++)
		for (int k=0; k<Buttons[0].length; k++)
		{
			Buttons[n][k].setText(Options.Keys[n][k]);
			Buttons[n][k].setState(0);
		}

		showSelf();

		if (Result) return Options;
	}
	catch(Exception e)
	{
		Log.err(getClass().getName()+".showBox() error : "+e);
	}

	return null;
}
}
