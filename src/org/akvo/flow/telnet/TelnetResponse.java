package org.akvo.flow.telnet;

import java.io.BufferedReader;
import java.io.IOException;

public class TelnetResponse
{
	private enum ResponseType
	{
		OK, KO;
	}

	private ResponseType responseType;
	private String message = null;

	public TelnetResponse(BufferedReader in) throws IOException
	{
		StringBuilder sb = new StringBuilder();

		// read first line
		String input = in.readLine();

		// KO check
		String koString = "KO: ";
		if (input.startsWith(koString))
		{
			responseType = ResponseType.KO;
			message = input.substring(koString.length());
			return;
		}

		// loop through all messages
		while (input.equals("OK") == false)
		{
			// save line
			sb.append(input).append('\n');
			// read new line
			input = in.readLine();
		}

		// remove last newline character
		if (sb.length() > 0)
		{
			if (sb.charAt(sb.length() - 1) == '\n')
			{
				sb.setLength(sb.length() - 1);
			}
			message = sb.toString();
		}
		responseType = ResponseType.OK;
	}

	public boolean isOk()
	{
		return getResponseType().equals(ResponseType.OK);
	}

	public ResponseType getResponseType()
	{
		return responseType;
	}

	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return "[" + getResponseType() + "] " + getMessage();
	}
}
