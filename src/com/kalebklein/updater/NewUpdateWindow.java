package com.kalebklein.updater;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class NewUpdateWindow extends JFrame
{
	private static final long serialVersionUID = -7534048294189283465L;
	
	private JFrame context, contextFrom;
	private JPanel contentPane;
	private JButton update, changelog;
	private boolean cmd;

	public NewUpdateWindow(JFrame context, boolean cmd)
	{
		super("Check for Updates");
		this.context = this;
		this.contextFrom = context;
		this.cmd = cmd;
		initializeWindow();
	}
	
	private void initializeWindow()
	{
		Dimension size = new Dimension(280, 150);
		setSize(size);
		setResizable(false);
		setLocationRelativeTo(null);
		
		if(cmd)
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel message = new JLabel("An update is available!");
		message.setBounds(60, 10, 200, 14);
		contentPane.add(message);
		
		changelog = new JButton("See What's New");
		changelog.setBounds(10, 35, 260, 30);
		changelog.addActionListener(new ActionHandler());
		contentPane.add(changelog);
		
		update = new JButton("Install Update");
		update.setBounds(10, 80, 260, 30);
		update.addActionListener(new ActionHandler());
		contentPane.add(update);
	}
	
	public void run()
	{
		setVisible(true);
	}
	
	private class ActionHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == update)
			{
				if(!cmd)
				{
					new UpdateWindow();
					context.dispose();
					contextFrom.dispose();
				}
				else
				{
					new UpdateWindow(true);
					context.dispose();
				}
			}
			else if(e.getSource() == changelog)
			{
				String url ="http://cdn.kalebklein.com/pc/update.txt";
				try
				{
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
				}
				catch (IOException ex)
				{
					JOptionPane.showMessageDialog(context, "Error opening changelog. Please try again later.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
