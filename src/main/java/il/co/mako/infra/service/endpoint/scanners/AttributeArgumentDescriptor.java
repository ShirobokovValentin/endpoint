package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.exceptions.TypeConversionException;
import il.co.mako.infra.service.endpoint.constraint.entity.SimpleViolation;
import il.co.mako.infra.service.endpoint.constraint.entity.Violation;
import il.co.mako.infra.service.endpoint.constraint.exceptions.ViolationException;
import il.co.mako.infra.service.endpoint.entity.ArgumentDescriptor;
import il.co.mako.infra.service.endpoint.readers.AttrReader;

import javax.servlet.http.HttpServletRequest;

public class AttributeArgumentDescriptor extends ArgumentDescriptor
{
	private AttrReader attrReader;

	// ------------------------------------------------------------

	public Object getArgument(HttpServletRequest request) throws ViolationException
	{
		try
		{
			Object convertedParamValue = getAttrReader().read(request);
			Violation violation = getConstraint().check(convertedParamValue);

			if (violation != null)
				throw new ViolationException(getAttrReader().getParamName(), violation);

			return convertedParamValue;

		} catch (TypeConversionException e)
		{
			throw new ViolationException(getAttrReader().getParamName(), new SimpleViolation(e));
		}
	}

	// ------------------------------------------------------------

	public AttrReader getAttrReader()
	{
		return this.attrReader;
	}

	public void setAttrReader(AttrReader attrReader)
	{
		this.attrReader = attrReader;
	}

}
