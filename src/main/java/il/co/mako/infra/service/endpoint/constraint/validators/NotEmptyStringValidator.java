package il.co.mako.infra.service.endpoint.constraint.validators;

import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;

public class NotEmptyStringValidator extends AbstractValidator implements ParamValidator
{
	public SimpleViolation check(Object obj)
	{
		if ((obj instanceof String) && !((String) obj).isEmpty())
			return null;
		return new SimpleViolation(getMessage());
	}
}
