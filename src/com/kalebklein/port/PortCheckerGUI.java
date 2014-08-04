package com.kalebklein.port;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.kalebklein.updater.UpdateCheck;

public class PortCheckerGUI extends JFrame
{
	private static final long serialVersionUID = -295187810247799343L;
	private JPanel contentPane;
	
	private JMenuBar menuBar;
	private JMenu file, help;
	private JMenuItem exit, about, update;
	
	private JTextField hnField, portField;
	
	private JButton check;
	
	PortCheckerGUI context = this;
	
	public interface Callback
	{
		void onCloseOperation();
	}
	
	private Callback callback;
	
	public PortCheckerGUI() {
		super("Port Checker");
		begin();
	}
	
	private void begin()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Dimension size = new Dimension(300, 210);
		setSize(size);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		prepareWindowElements();
	}
	
	private void prepareWindowElements()
	{
		menuBar = new JMenuBar();
		file = new JMenu("File");
		help = new JMenu("Help");
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionHandler());
		file.add(exit);
		
		about = new JMenuItem("About");
		about.addActionListener(new ActionHandler());
		help.add(about);
		
		update = new JMenuItem("Check for Updates");
		update.addActionListener(new ActionHandler());
		help.add(update);
		
		menuBar.add(file);
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel hnText = new JLabel("Hostname:");
		hnText.setBounds(10, 10, 200, 12);
		contentPane.add(hnText);
		
		hnField = new JTextField();
		hnField.setBounds(10, 25, 215, 30);
		contentPane.add(hnField);
		
		JLabel pText = new JLabel("Port Number:");
		pText.setBounds(10, 60, 200, 12);
		contentPane.add(pText);
		
		portField = new JTextField();
		portField.setBounds(10, 75, 100, 30);
		contentPane.add(portField);
		
		check = new JButton("Check Port");
		check.setBounds(10, 115, 260, 30);
		check.addActionListener(new ActionHandler());
		contentPane.add(check);
	}
	
	public void run(Callback callback)
	{
		setVisible(true);
		this.callback = callback;
	}
	
	protected int parsePort(String port)
	{
		try
		{
			return Integer.parseInt(port);
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	private class ActionHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			final String hostname = hnField.getText();
			final int port = parsePort(portField.getText());
			
			Object src = e.getSource();
			
			if(src == exit)
			{
				callback.onCloseOperation();
			}
			else if(src == about)
			{
				JOptionPane.showMessageDialog(context, "A simple port checker written in Java\n\nVersion: " + Main.VERSION, "About", JOptionPane.PLAIN_MESSAGE);
			}
			else if(src == update)
			{
				new UpdateCheck(context).run();
			}
			else if(src == check)
			{
				if(hostname.equals(""))
				{
					JOptionPane.showMessageDialog(context, "Error: You must supply a hostname!", "Hostname Empty", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if(port <= 0)
					{
						JOptionPane.showMessageDialog(context, "Error: You must supply a valid port number!", "Port Parsing Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						check.setText("Checking, please wait...");
						check.setEnabled(false);
						hnField.setEnabled(false);
						portField.setEnabled(false);
						
						Thread thread = new Thread()
						{
							@Override
							public void run()
							{
								String message = "";
								
								if(new PortChecker(hostname, port).run())
									message = String.format("The port: %s is open on host: %s.\nConnection success.", port, hostname);
								else
									message = String.format("The port: %s is unreachable on host: %s.\nConnection failure.", port, hostname);
								
								JOptionPane.showMessageDialog(context, message, "Results", JOptionPane.INFORMATION_MESSAGE);
								
								check.setText("Check Port");
								check.setEnabled(true);
								hnField.setEnabled(true);
								portField.setEnabled(true);
							}
						};
						
						thread.start();
					}
				}
			}
		}
	}
}
