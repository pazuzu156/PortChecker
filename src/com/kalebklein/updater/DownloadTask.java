package com.kalebklein.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DownloadTask extends SwingWorker<Void, Void>
{
	private static final int BUFFER_SIZE = 4096;
	private String downloadURL, saveDirectory;
	private UpdateWindow gui;

	public DownloadTask(UpdateWindow gui, String downloadURL, String saveDirectory)
	{
		this.gui = gui;
		this.downloadURL = downloadURL;
		this.saveDirectory = saveDirectory;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		try
		{
			HttpDownloadUtil util = new HttpDownloadUtil();
			util.downloadFile(downloadURL);
			String saveFilePath = saveDirectory + File.separator + util.getFileName();

			InputStream inputStream = util.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;
			long fileSize = util.getContentLength();

			while((bytesRead = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);
				setProgress(percentCompleted);
			}

			outputStream.close();
			util.disconnect();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(gui, "Error downloading file: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			setProgress(0);
			cancel(true);
		}

		return null;
	}

	@Override
	protected void done()
	{
		if(!isCancelled())
			JOptionPane.showMessageDialog(gui, "Port Checker has been updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}
}
