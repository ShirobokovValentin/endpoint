package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.annotation.parameter.AttributeReaderClass;
import il.co.mako.infra.service.endpoint.annotation.parameter.AttributeReaderFactoryMethod;
import il.co.mako.infra.service.endpoint.entity.RawArgumentDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.endpoint.exceptions.InvalidDefinitionException;
import il.co.mako.infra.service.endpoint.readers.AttrReader;
import il.co.mako.infra.service.MakoReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class AttributeReaderScanner extends LinearAnnotationParamScanner<AttrReader, String>
{
	@Override
	protected Class<? extends Annotation> getWorkerClassAnnotation()
	{
		return AttributeReaderClass.class;
	}

	@Override
	protected Class<? extends Annotation> getFactoryAnnotation()
	{
		return AttributeReaderFactoryMethod.class;
	}

	@Override
	protected void findAdditionalValue(Annotation annotation, RawArgumentDescriptor<AttrReader, String> descriptor) throws InvalidDefinitionException
	{
	}

	@Override
	protected AttrReader createWorker(List<RawArgumentDescriptor<AttrReader, String>> descriptors, Class<?> argumentType)
	{
		if (descriptors == null || descriptors.isEmpty())
			return null;

		if (descriptors.size() != 1)
			throw new IncompatibleAnnotationException();

		RawArgumentDescriptor<AttrReader, String> descriptor = descriptors.get(FIRST_ELEMENT);

		Class<?> readerClass = descriptor.getWorkerClass();

		String factoryMethod = descriptor.getFactoryMethod();
		Object params = descriptor.getCreationParams();

		AttrReader reader = (AttrReader) MakoReflectionUtils.getNewInstance(readerClass, factoryMethod, params);

		return reader;
	}

}
