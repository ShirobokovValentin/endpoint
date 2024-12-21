package il.co.mako.infra.service.endpoint.entity;

import il.co.mako.infra.service.endpoint.constraint.exceptions.ViolationException;
import il.co.mako.infra.service.endpoint.constraint.validators.ComplexValidator;

import javax.servlet.http.HttpServletRequest;

public abstract class ArgumentDescriptor
{
	private ComplexValidator constraint;

	// ------------------------------------------------------------

	public abstract Object getArgument(HttpServletRequest request) throws ViolationException;

	// ------------------------------------------------------------

	public ComplexValidator getConstraint()
	{
		return this.constraint;
	}

	public void setConstraint(ComplexValidator constraint)
	{
		this.constraint = constraint;
	}
}