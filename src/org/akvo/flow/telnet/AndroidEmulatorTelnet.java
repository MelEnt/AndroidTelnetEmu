package org.akvo.flow.telnet;

import java.io.IOException;

public class AndroidEmulatorTelnet extends Telnet
{
	private String authPath;

	public AndroidEmulatorTelnet(String ip) throws IOException, InterruptedException
	{
		this(ip, 5554);
	}

	public AndroidEmulatorTelnet(String ip, int port) throws IOException, InterruptedException
	{
		this(ip, port, 3);
	}

	public AndroidEmulatorTelnet(String ip, int port, int retries)
			throws IOException, InterruptedException
	{
		super(ip, port, retries);
		//Retrieve path of auth token
		TelnetResponse welcomingMessage = receive();
		String[] lines = welcomingMessage.getMessage().split("\n");
		authPath = lines[lines.length - 1];
		authPath = authPath.substring(1, authPath.length() - 1);
	}

	public String getAuthPath()
	{
		return authPath;
	}

	/* EMULATOR telnet functions */
	public void authenticate(String authToken) throws IOException, EmulatorAuthException
	{
		TelnetResponse loginMessage = communicate("auth " + authToken);
		if (loginMessage.isOk() == false)
		{
			throw new EmulatorAuthException(loginMessage.getMessage());
		}
	}

	/**
	 * Send "geo fix lon lat" command through telnet to be picked up by
	 * connected emulator
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 * @throws IOException
	 */
	public TelnetResponse geo(double lon, double lat) throws IOException
	{
		if ((lon > 180.0) || (lon < -180.0))
		{
			throw new IllegalArgumentException("invalid longitude value. Should be in [-180,+180] range");
		}
		if ((lat > 90.0) || (lat < -90.0))
		{
			throw new IllegalArgumentException("invalid latitude value. Should be in [-90,+90] range");
		}

		return communicate("geo fix " + lon + " " + lat);
	}
}
