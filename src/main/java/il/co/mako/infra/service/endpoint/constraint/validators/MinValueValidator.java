package il.co.mako.infra.service.endpoint.constraint.validators;

import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;

public class MinValueValidator extends AbstractValidator implements ParamValidator
{
	private final int minValue;

	// ------------------------------------------------------------

	public MinValueValidator(Integer minValue)
	{
		this.minValue = minValue;
	}

	public SimpleViolation check(Object obj)
	{
		if (obj == null)
			return null;

		if (!(obj instanceof Integer))
			return null;

		Integer inspectedObject = (Integer) obj;

		if (inspectedObject > minValue)
			return null;

		return new SimpleViolation("must be not less then " + minValue + " but was " + String.valueOf(inspectedObject));

	}

}
