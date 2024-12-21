package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.ParamConvertor;
import il.co.mako.infra.service.endpoint.constraint.ParamConstraintScanner;
import il.co.mako.infra.service.endpoint.constraint.validators.ComplexValidator;
import il.co.mako.infra.service.endpoint.entity.ArgumentDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.endpoint.exceptions.InvalidMappingException;
import il.co.mako.infra.service.endpoint.exceptions.RequredAnnotationMissingException;
import il.co.mako.infra.service.endpoint.readers.AttrReader;
import il.co.mako.infra.service.endpoint.readers.ParamReader;
import il.co.mako.infra.service.endpoint.readers.RawRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public class ArgumentScanner
{
	private ParamReaderScanner paramReaderScanner = new ParamReaderScanner();

	private ParamConvertorScanner convertorScanner = new ParamConvertorScanner();

	private ParamConstraintScanner constraintsScanner = new ParamConstraintScanner();

	private AttributeReaderScanner attributeReaderScanner = new AttributeReaderScanner();

	// ------------------------------------------------------------

	public ArgumentDescriptor scan(Annotation[] annotations, Class<?> argumentType) throws InvalidMappingException
	{

		if (argumentType == HttpServletRequest.class)
		{
			AttributeArgumentDescriptor argDescriptor = new AttributeArgumentDescriptor();
			argDescriptor.setAttrReader(new RawRequest());
			argDescriptor.setConstraint(new ComplexValidator());
			return argDescriptor;
		}

		ComplexValidator constraint = constraintsScanner.scan(annotations, argumentType);
		ParamReader paramReader = paramReaderScanner.scan(annotations, argumentType);
		AttrReader attrReader = attributeReaderScanner.scan(annotations, argumentType);

		if (paramReader != null && attrReader != null)
		{
			throw new IncompatibleAnnotationException();
		}

		if (paramReader != null)
		{
			ParamConvertor<?> paramConvertor = convertorScanner.scan(annotations, argumentType);

			ParamArgumentDescriptor argDescriptor = new ParamArgumentDescriptor();
			argDescriptor.setParamReader(paramReader);
			argDescriptor.setParamConvertor(paramConvertor);
			argDescriptor.setConstraint(constraint);
			return argDescriptor;
		}

		if (attrReader != null)
		{
			AttributeArgumentDescriptor argDescriptor = new AttributeArgumentDescriptor();
			argDescriptor.setAttrReader(attrReader);
			argDescriptor.setConstraint(constraint);
			return argDescriptor;
		}

		throw new RequredAnnotationMissingException()
				.annotation("@ParamReaderClass")
				.annotation("@AttributeReaderClass");
	}

}