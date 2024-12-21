package il.co.mako.infra.service.endpoint.exceptions;

public class InvalidMappingException extends EndpointException
{
	private static final long serialVersionUID = 211190057872446369L;

	// ------------------------------------------------------------

	public InvalidMappingException()
	{
		super();
	}

	public InvalidMappingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidMappingException(String message)
	{
		super(message);
	}

	public InvalidMappingException(Throwable cause)
	{
		super(cause);
	}

}
