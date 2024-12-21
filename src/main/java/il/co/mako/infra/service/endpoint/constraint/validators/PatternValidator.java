package il.co.mako.infra.service.endpoint.constraint.validators;

import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;
import il.co.mako.infra.service.endpoint.exceptions.InvalidDefinitionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator extends AbstractValidator implements ParamValidator
{
	private final Pattern pattern;

	// ------------------------------------------------------------

	public PatternValidator(String patternString)
	{
		pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
	}

	// ------------------------------------------------------------

	public static PatternValidator getInstance(String args)
	{
		if (args == null)
			throw new InvalidDefinitionException("Can't create PatternValidator");
		return new PatternValidator(args);
	}

	public SimpleViolation check(Object obj)
	{
		if (!(obj instanceof String))
			return null;

		Matcher matcher = pattern.matcher((String) obj);
		if (matcher.find())
			return null;
		
		return new SimpleViolation( "must match the pattern: " + pattern.toString());

	}

}
