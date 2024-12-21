package il.co.mako.infra.service.endpoint.constraint.exceptions;

import il.co.mako.infra.service.endpoint.constraint.entity.Violation;
import il.co.mako.infra.service.endpoint.exceptions.EndpointException;

public class ViolationException extends EndpointException
{
	private static final long serialVersionUID = 4142180372888637778L;

	// ------------------------------------------------------------

	private final String paramName;

	private final Violation violation;

	public ViolationException(String paramName, Violation violation)
	{
		this.paramName = paramName;
		this.violation = violation;
	}

	// ------------------------------------------------------------

	public String getParamName()
	{
		return this.paramName;
	}

	public Violation getViolation()
	{
		return this.violation;
	}

}
