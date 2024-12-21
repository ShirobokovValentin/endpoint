package il.co.mako.infra.service.convertors.exceptions;

import il.co.mako.infra.service.MakoException;

public class ConvertorException extends MakoException
{

	private static final long serialVersionUID = -6911495157354783727L;

	// ------------------------------------------------------------

	public ConvertorException()
	{
		super();
	}

	public ConvertorException(Throwable cause)
	{
		super(cause);
	}

	public ConvertorException(String message)
	{
		super(message);
	}

	public ConvertorException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
