package il.co.mako.infra.service.endpoint.constraint.exceptions;

import il.co.mako.infra.service.endpoint.constraint.entity.Conclusion;
import il.co.mako.infra.service.endpoint.exceptions.EndpointException;

public class ArgumentValidationException extends EndpointException
{
	private static final long serialVersionUID = -8208681278594257248L;

	private Conclusion conclusion;

	// ------------------------------------------------------------

	public ArgumentValidationException(Conclusion conclusion)
	{
		this(conclusion.getMessage());
		this.conclusion = conclusion;
	}

	public ArgumentValidationException()
	{
		super();
	}

	public ArgumentValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ArgumentValidationException(String message)
	{
		super(message);
	}

	public ArgumentValidationException(Throwable cause)
	{
		super(cause);
	}

	// ------------------------------------------------------------

	public Conclusion getConclusion()
	{
		return this.conclusion;
	}

	public void setConclusion(Conclusion conclusion)
	{
		this.conclusion = conclusion;
	}

}
