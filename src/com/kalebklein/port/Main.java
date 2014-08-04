package com.kalebklein.port;

import com.kalebklein.updater.UpdateCheck;

public class Main
{
	public static final int VERSION_CODE = 2;
	public static final String VERSION = "1.0";

	private String hostname;
	private int port;

	public Main(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
	}

	public void test()
	{
		if(new PortChecker(this.hostname, this.port).run())
			System.out.println(String.format("The port: %s is open on host: %s.\nConnection success.\n", this.port, this.hostname));
		else
			System.out.println(String.format("The port: %s is unreachable on host: %s.\nConnection failure.\n", this.port, this.hostname));
	}

	public static void main(String[] args)
	{
		System.out.println(String.format("Port Checker Command Line Tool\n", VERSION));

		if(args.length == 0)
		{
			System.out.println("Usage for this app are as follows:\n\n"
					+ "portChecker.jar [hostname] [port](defaults to 80 if left empyt)\n"
					+ "Example: java -jar portChecker.jar www.example.com 80\n\n"
					+ "use flag --help to show the commands for this app.");
		}
		else if(args.length == 1)
		{
			String flag = args[0];

			if(flag.startsWith("-") || flag.startsWith("--"))
			{
				if(flag.equals("--help") || flag.equals("-h"))
				{
					String help = "Here is the list of flags Port Checker accepts:\n"
							+ "--help | -h     - Shows this help menu\n"
							+ "--gui           - Displays the GUI interface\n"
							+ "--update        - Checks for updates and updates Port Checker\n"
							+ "--version | -v  - Displays version and copyright info\n";

					System.out.println(help);
				}
				else if(flag.equals("--gui"))
				{
					System.out.println("Opening GUI..");
					new PortCheckerGUI().run(new PortCheckerGUI.Callback()
					{
						@Override
						public void onCloseOperation()
						{
							System.out.println("Closing GUI..");
							System.exit(0);
						}
					});
				}
				else if(flag.equals("--update"))
					new UpdateCheck(true).run();
				else if(flag.equals("--version") || flag.equals("-v"))
					System.out.println("Port Checker v" + VERSION + " build: " + VERSION_CODE
							+ "\n(c) 2014 Kaleb Klein.\nAll Rights Reserved.\n");
				else
					System.out.println(String.format("'%s' is not a valid command!\n", flag));
			}
			else
			{
				System.out.println(String.format("Checking if port: %s is open...\n", 80));
				new Main(flag, 80).test();
			}
		}
		else if(args.length > 1)
		{
			if(args[0].startsWith("-") || args[1].startsWith("-"))
			{
				if(args[0].equals("-h") || args[0].equals("--help"))
				{
					if(args[1].equals("--help") || args[1].equals("-h"))
						System.out.println("Shows the help message\n");

					else if(args[1].equals("--gui")) System.out.println("Display the GUI interface for Port Checker.\nIf you're on Windows,"
							+ " the .exe included automatically adds this flag, thus auto starting the GUI\n");

					else if(args[1].equals("--update")) System.out.println("This flag force checks for updates and updates with a GUI. \n"
							+ "No command line updater is included with this app.\n");

					else if(args[1].equals("--version") || args[1].equals("-v")) System.out.println("This flag displays version information and copyright notices.\n");

					else
						System.out.println("Error: '" + args[1] + "' is not a valid command! No help information can be found.\n");
				}
				else
					System.out.println("Error: Flags cannot be passed as hostnames or ports!\n");
			}
			else
			{
				String hostname = args[0];
				int port = 80;

				try
				{
					port = Integer.parseInt(args[1]);

					System.out.println(String.format("Checking if port: %s is open...\n", port));
					new Main(hostname, port).test();
				}
				catch(Exception e)
				{
					System.out.println(String.format("Error: '%s' is not a valid port number!\n", args[1]));
				}
			}
		}

		System.out.println("Thank you for using Port Checker!");
	}
}
