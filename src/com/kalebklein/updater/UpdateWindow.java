package com.kalebklein.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.kalebklein.port.PortCheckerGUI;

public class UpdateWindow extends JFrame implements PropertyChangeListener
{
	private static final long serialVersionUID = 4144912042032065096L;
	
	private JPanel contentPane;
	private JProgressBar progressBar;
	private static JButton btnRestart;
	private JLabel lblInfo;
	UpdateWindow context;
	private boolean cmd = false;
	
	public UpdateWindow(boolean cmd)
	{
		this.cmd = cmd;
		this.context = this;
		initializeWindow();
	}
	
	public UpdateWindow()
	{
		this.context = this;
		initializeWindow();
	}
	
	public void initializeWindow()
	{
		setTitle("Updating...");
		setResizable(false);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(450, 146);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblInfo = new JLabel("Updating PortChecker. Please wait...");
		lblInfo.setBounds(10, 11, 414, 25);
		contentPane.add(lblInfo);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 36, 414, 25);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar);
		
		btnRestart = new JButton("Restart Port Checker");
		btnRestart.setEnabled(false);
		btnRestart.setBounds(10, 72, 414, 26);
		contentPane.add(btnRestart);
		
		setVisible(true);
		
		btnRestart.addActionListener(new ActionHandler());
		beginUpdate();
	}
	
	private void restartApplication()
	{
		try
		{
			ArrayList<String> command = new ArrayList<String>();
			
			File currentJar = new File(PortCheckerGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				
			if(!currentJar.getName().endsWith(".jar"))
			{
				if(System.getProperty("os.name").contains("Windows"))
					command.add("PortChecker.exe");
			}
			else
			{
				String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
				command.add(javaBin);
				command.add("-jar");
				command.add(currentJar.getPath());
				command.add("--gui");
			}
			
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(context, "Error opening Port Checker! Everything is good though, just manually repoen is.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		System.exit(0);
	}
	
	private void beginUpdate()
	{
		String os = System.getProperty("os.name");
		String downloadURL = null;
		
		// Check for Windows. Windows uses .exe, so it mau use .jar or .exe. Make sure both are take care of
		if(os.contains("Windows"))
		{
			try
			{
				File currentJar = new File(PortCheckerGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				if(currentJar.getName().endsWith(".jar"))
				{
					downloadURL = "http://cdn.kalebklein.com/pc/updates/portChecker.jar";
				}
				else if(currentJar.getName().endsWith(".exe"))
				{
					downloadURL = "http://cdn.kalebklein.com/pc/updates/PortChecker.exe";
				}
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		else
			downloadURL = "http://cdn.kalebklein.com/pc/updates/portChecker.jar";
		
		String saveDir = System.getProperty("user.dir");
		
		try
		{
			progressBar.setValue(0);
			
			DownloadTask task = new DownloadTask(context, downloadURL, saveDir + "/test");
			task.addPropertyChangeListener(context);
			task.execute();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(context, "Error executing download task: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals("progress"))
		{
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			if(progress == 100)
			{
				lblInfo.setText("Update Complete!");
				btnRestart.setEnabled(true);
				
				if(cmd)
				{
					btnRestart.setText("Close Updater");
				}
			}
		}
	}
	
	private class ActionHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == btnRestart)
			{
				if(cmd)
				{
					System.exit(0);
				}
				else
				{
					restartApplication();
				}
			}
		}
		
	}
}
