package com.kalebklein.updater;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Changelog extends JFrame
{
	private static final long serialVersionUID = -6319765470096085176L;
	
	private JPanel contentPane;
	private JButton close;
	JTextArea txtChangeLog;
	
	public Changelog()
	{
		super("What's New");
		initializeWindow();
	}
	
	private void initializeWindow()
	{
		Dimension size = new Dimension(400, 305);
		setResizable(false);
		setSize(size);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtChangeLog = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtChangeLog);
		DefaultCaret caret = (DefaultCaret) txtChangeLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		scrollPane.setBounds(10, 12, 374, 213);
		txtChangeLog.setBackground(UIManager.getColor("TextField.inactiveBackground"));
		txtChangeLog.setEditable(false);
		txtChangeLog.setBounds(10, 12, 374, 213);
		txtChangeLog.setLineWrap(true);
		txtChangeLog.setWrapStyleWord(true);
		contentPane.add(scrollPane);
		getChangeLog();
		
		close = new JButton("Close");
		close.setBounds(10, 235, 374, 30);
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		contentPane.add(close);
	}
	
	private void getChangeLog()
	{
		String url_string = "http://cdn.kalebklein.com/pc/update.txt";
		String clMessage = "";
		try
		{
			URL url = new URL(url_string);
			InputStream in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null)
			{
				builder.append(line + "\n");
			}
			reader.close();
			in.close();
			clMessage = builder.toString();
		}
		catch(Exception e)
		{
			clMessage = "There was an error grabbing the changelog. Please try again later.";
		}
		txtChangeLog.setText(clMessage);
		txtChangeLog.setCaretPosition(0);
	}
	
	public void run()
	{
		setVisible(true);
	}
}

