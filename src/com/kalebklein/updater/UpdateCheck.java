package com.kalebklein.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.kalebklein.port.Main;
import com.kalebklein.port.Connectivity;

public class UpdateCheck implements Runnable
{
	private JFrame context;
	boolean cmd = false;

	// For Command Line update. No context is passed into this one.
	public UpdateCheck(boolean cmd)
	{
		this.context = null;
		this.cmd = cmd;
	}

	public UpdateCheck(JFrame context)
	{
		this.context = context;
	}

	@Override
	public void run()
	{
		Connectivity con = Connectivity.getInstance().execute();
		if(con.isConnected())
		{
			try
			{
				URL url = new URL("http://cdn.kalebklein.com/pc/version.txt");
				InputStream in = url.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder builder = new StringBuilder();
				String line = "";
				while((line = reader.readLine()) != null)
				{
					builder.append(line);
				}
				reader.close();
				in.close();
				line = builder.toString();

				int version_code = Integer.parseInt(line);

				if(Main.VERSION_CODE < version_code)
				{
					if(!cmd)
						new NewUpdateWindow(context, false).run();
					else
						new NewUpdateWindow(context, true).run();
				}
				else
					JOptionPane.showMessageDialog(context, "You are currently up to date!", "Check for Updates", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(MalformedURLException e)
			{
				JOptionPane.showMessageDialog(context, "Error Code: 0x000001\n\nIf you see this, contact me and let me know the error code being displayed.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(context, "Error connecting to the update server! Please try again later.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
			JOptionPane.showMessageDialog(context, "You are currently up to date!", "Check for Updates", JOptionPane.INFORMATION_MESSAGE);
	}
}
