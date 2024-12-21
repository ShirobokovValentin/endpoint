package il.co.mako.infra.service.endpoint.constraint.validators;

import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;

public class NotNullValidator extends AbstractValidator implements ParamValidator
{
	public SimpleViolation check(Object obj)
	{
		if (obj != null)
			return null;

		return new SimpleViolation(getMessage() != null ? getMessage() : "Can't be null");
	}

}
