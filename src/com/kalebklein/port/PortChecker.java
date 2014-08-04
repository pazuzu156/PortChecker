package com.kalebklein.port;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

public class PortChecker
{
	private String hostname;
	private int port;
	
	public PortChecker(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
	}
	
	public boolean run()
	{
		try
		{
			Socket sock = SocketFactory.getDefault().createSocket();
            sock.connect(new InetSocketAddress(hostname, port), 4000);
            sock.close();
            return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}
}
