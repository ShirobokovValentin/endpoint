package il.co.mako.infra.service.endpoint.constraint.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Conclusion
{
	private final Map<String, Violation> violations = new HashMap<String, Violation>();

	// ------------------------------------------------------------

	public void put(String paramName, Violation constraintViolation)
	{
		if (constraintViolation == null)
			return;

		violations.put(paramName, constraintViolation);
	}

	public boolean hasViolation()
	{
		if (violations.isEmpty())
			return false;

		return true;
	}

	public String getMessage()
	{
		StringBuilder builder = new StringBuilder();

		if (!hasViolation())
		{
			return "Has no violation";
		}

		boolean firstParam = true;
		for (Entry<String, Violation> entry : violations.entrySet())
		{
			String paramName = entry.getKey();
			Violation violation = entry.getValue();

			if (firstParam)
				firstParam = false;
			else
				builder.append("; "); // TODO Array of masseges?

			StringBuilder forParam = new StringBuilder("Parameter " + paramName + ": ");

			String message = violation.getMessage();

			if (message != null)
				forParam.append(message);

			builder.append(forParam.toString());
		}

		return builder.toString();
	}
}
