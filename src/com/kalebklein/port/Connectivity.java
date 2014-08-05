package com.kalebklein.port;

import java.io.InputStream;
import java.net.URL;

public class Connectivity
{
    private static Connectivity instance;
    private boolean isConnected;

    private Connectivity()
    {
        this.isConnected = false;
    }

    public static Connectivity getInstance()
    {
        if(instance == null)
            return instance = new Connectivity();
        else
            return instance;
    }

    public Connectivity execute()
    {
        try
        {
            URL url = new URL("http://www.google.com");
            InputStream in = url.openStream();
            this.isConnected = true;
            in.close();
        }
        catch(Exception e) {}
        return this;
    }

    public boolean isConnected()
    {
        return this.isConnected;
    }
}
