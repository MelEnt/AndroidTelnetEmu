package org.akvo.flow.telnet;

//** TODO: separate new socket, change to Timber logs, maybe remove retries,
// TODO: add more functionality and copyright headers
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Telnet implements Closeable
{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public Telnet(String ip, int port, int retries) throws IOException, InterruptedException
	{
		System.out.println("Connecting to " + ip + " (port: " + port + ")");

		int attempt = 1;
		while (true)
		{
			if (attempt > retries)
			{
				throw new ConnectException("Telnet failed to connect. Check your address and make sure emulator is running");
			}
			try
			{
				socket = new Socket(ip, port);
			}
			catch (ConnectException e)
			{
				System.err.println("attempt #" + attempt + " failed: " + e.getMessage());
				attempt++;
				Thread.sleep(500);
				continue;
			}
			break;
		}

		System.out.println("Telnet sucessfully connected");

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
	}

	public void send(String text)
	{
		out.write(text);
		out.write('\n');
		out.flush();
	}

	public TelnetResponse receive() throws IOException
	{
		return new TelnetResponse(in);
	}

	public TelnetResponse communicate(String text) throws IOException
	{
		// send
		send(text);
		// receive
		return receive();
	}

	@Override
	public void close() throws IOException
	{
		System.out.println("Telnet disconnecting");
		socket.close();
	}
}
