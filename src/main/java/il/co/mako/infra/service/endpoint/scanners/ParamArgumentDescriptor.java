package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.ParamConvertor;
import il.co.mako.infra.service.convertors.exceptions.TypeConversionException;
import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;
import il.co.mako.infra.service.endpoint.constraint.entity.Violation;
import il.co.mako.infra.service.endpoint.constraint.exceptions.ViolationException;
import il.co.mako.infra.service.endpoint.entity.ArgumentDescriptor;
import il.co.mako.infra.service.endpoint.readers.ParamReader;

import javax.servlet.http.HttpServletRequest;

public class ParamArgumentDescriptor extends ArgumentDescriptor
{
	private ParamReader paramReader;

	private ParamConvertor<?> paramConvertor;

	// ------------------------------------------------------------

	public Object getArgument(HttpServletRequest request) throws ViolationException
	{
		try
		{
			String rawParamValue = getParamReader().read(request);
			Object convertedParamValue = getParamConvertor().fromString(rawParamValue);
			Violation violation = getConstraint().check(convertedParamValue);

			if (violation != null)
				throw new ViolationException(getParamReader().getParamName(), violation);

			return convertedParamValue;

		} catch (TypeConversionException e)
		{
			throw new ViolationException(getParamReader().getParamName(), new SimpleViolation(e));
		}
	}

	// ------------------------------------------------------------

	public ParamReader getParamReader()
	{
		return this.paramReader;
	}

	public void setParamReader(ParamReader paramReader)
	{
		this.paramReader = paramReader;
	}

	public ParamConvertor<?> getParamConvertor()
	{
		return this.paramConvertor;
	}

	public void setParamConvertor(ParamConvertor<?> paramConvertor)
	{
		this.paramConvertor = paramConvertor;
	}
}
