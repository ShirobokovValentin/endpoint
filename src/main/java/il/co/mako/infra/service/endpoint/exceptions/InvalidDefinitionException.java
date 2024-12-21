package il.co.mako.infra.service.endpoint.exceptions;

public class InvalidDefinitionException extends EndpointException
{

	private static final long serialVersionUID = 7408253166290033766L;
	
	// ------------------------------------------------------------

	public InvalidDefinitionException()
	{
		super();
	}

	public InvalidDefinitionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidDefinitionException(String message)
	{
		super(message);
	}

	public InvalidDefinitionException(Throwable cause)
	{
		super(cause);
	}

}
