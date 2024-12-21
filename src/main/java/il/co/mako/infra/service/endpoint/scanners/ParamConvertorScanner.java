package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.ParamConvertor;
import il.co.mako.infra.service.convertors.ConvertorsStoke;
import il.co.mako.infra.service.endpoint.annotation.parameter.ParamConvertorClass;
import il.co.mako.infra.service.endpoint.annotation.parameter.ParamConvertorFactoryMethod;
import il.co.mako.infra.service.endpoint.entity.RawArgumentDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.MakoReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class ParamConvertorScanner extends LinearAnnotationParamScanner<ParamConvertor<?>, String>
{
	@Override
	protected Class<? extends Annotation> getWorkerClassAnnotation()
	{
		return ParamConvertorClass.class;
	}

	@Override
	protected Class<? extends Annotation> getFactoryAnnotation()
	{
		return ParamConvertorFactoryMethod.class;
	}

	@Override
	protected void findAdditionalValue(Annotation annotation, RawArgumentDescriptor<ParamConvertor<?>, String> descriptor)
	{
		//NOP
	}

	@Override
	protected ParamConvertor<?> createWorker(List<RawArgumentDescriptor<ParamConvertor<?>, String>> descriptors, Class<?> argumentType)
	{
		if (descriptors == null || descriptors.isEmpty())
			return ConvertorsStoke.getConvertors(argumentType);

		if (descriptors.size() != 1)
			throw new IncompatibleAnnotationException();

		RawArgumentDescriptor<ParamConvertor<?>, String> descriptor = descriptors.get(FIRST_ELEMENT);

		Class<?> convertorClass = descriptor.getWorkerClass();

		if (convertorClass == null)
			return ConvertorsStoke.getConvertors(argumentType);

		String factoryMethod = descriptor.getFactoryMethod();
		Object params = null;

		ParamConvertor<?> convertor = (ParamConvertor<?>) MakoReflectionUtils.getNewInstance(convertorClass, factoryMethod, params);

		return convertor;
	}

}
