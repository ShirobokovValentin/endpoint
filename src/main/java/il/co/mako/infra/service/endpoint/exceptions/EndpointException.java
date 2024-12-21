package il.co.mako.infra.service.endpoint.exceptions;

import il.co.mako.infra.service.MakoException;

public class EndpointException extends MakoException
{
	private static final long serialVersionUID = -4149413181576101647L;

	// ------------------------------------------------------------

	public EndpointException()
	{
		super();
	}

	public EndpointException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EndpointException(String message)
	{
		super(message);
	}

	public EndpointException(Throwable cause)
	{
		super(cause);
	}

}
