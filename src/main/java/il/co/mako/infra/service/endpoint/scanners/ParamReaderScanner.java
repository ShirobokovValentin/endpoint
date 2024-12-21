package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.annotation.parameter.ParamReaderClass;
import il.co.mako.infra.service.endpoint.annotation.parameter.ParamReaderFactoryMethod;
import il.co.mako.infra.service.endpoint.entity.RawArgumentDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.endpoint.exceptions.InvalidDefinitionException;
import il.co.mako.infra.service.endpoint.readers.ParamReader;
import il.co.mako.infra.service.MakoReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class ParamReaderScanner extends LinearAnnotationParamScanner<ParamReader, String>
{
	@Override
	protected Class<? extends Annotation> getWorkerClassAnnotation()
	{
		return ParamReaderClass.class;
	}

	@Override
	protected Class<? extends Annotation> getFactoryAnnotation()
	{
		return ParamReaderFactoryMethod.class;
	}

	@Override
	protected void findAdditionalValue(Annotation annotation, RawArgumentDescriptor<ParamReader, String> descriptor) throws InvalidDefinitionException
	{
		Object defaultValue = MakoReflectionUtils.getValue(annotation, DEFAULT_VALUE_METHOD_NAME);

		if (defaultValue == null)
			return;

		if (ParamReaderClass.DEFAULT_NONE.equals(defaultValue))
			return;

		if (!(defaultValue instanceof String))
			throw new InvalidDefinitionException("defaultValue for paramReader must be Strign.");

		String readerDefaultValue = (String) defaultValue;
		descriptor.setAdditionalValue(readerDefaultValue);
	}

	@Override
	protected ParamReader createWorker(List<RawArgumentDescriptor<ParamReader, String>> descriptors, Class<?> argumentType)
	{
		if (descriptors == null || descriptors.isEmpty())
			return null;

		if (descriptors.size() != 1)
			throw new IncompatibleAnnotationException();

		RawArgumentDescriptor<ParamReader, String> descriptor = descriptors.get(FIRST_ELEMENT);

		Class<?> readerClass = descriptor.getWorkerClass();
		if (readerClass == null)
			return null;

		String factoryMethod = descriptor.getFactoryMethod();
		Object params = descriptor.getCreationParams();
		String defaultValue = descriptor.getAdditionalValue();

		ParamReader reader = (ParamReader) MakoReflectionUtils.getNewInstance(readerClass, factoryMethod, params);
		reader.setDefaultValue(defaultValue);

		return reader;
	}

}
