package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.entity.RawArgumentDescriptor;
import il.co.mako.infra.service.MakoReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class AbstractParamScanner<WorkerType, AdditionalValueType>
{
	protected static final String DEFAULT_VALUE_METHOD_NAME = "defaultValue";

	protected static final String CREATION_PARAMS_METHOD_NAME = "value";

	protected static final String MESSAGE_STRING_METHOD_NAME = "message";

	protected static final int FIRST_ELEMENT = 0;

	// ------------------------------------------------------------

	protected void findCreationParams(Annotation annotation, RawArgumentDescriptor<WorkerType, AdditionalValueType> descriptor)
	{
		Object creationParams = MakoReflectionUtils.getValue(annotation, CREATION_PARAMS_METHOD_NAME);
		descriptor.setCreationParams(creationParams);
	}

	protected void findWorkerClass(AnnotatedElement currentInterface, RawArgumentDescriptor<WorkerType, AdditionalValueType> descriptor)
	{
		Annotation annotation = currentInterface.getAnnotation(getWorkerClassAnnotation());
		if (annotation != null)
		{
			Class<? extends WorkerType> value = MakoReflectionUtils.getValue(annotation);
			descriptor.setWorkerClass(value);
		}
	}

	protected abstract Class<? extends Annotation> getWorkerClassAnnotation();

	protected void findFactoryMethod(AnnotatedElement currentInterface, RawArgumentDescriptor<WorkerType, AdditionalValueType> descriptor)
	{
		Annotation annotation = currentInterface.getAnnotation(getFactoryAnnotation());
		if (annotation != null)
		{
			String value = MakoReflectionUtils.getValue(annotation);
			descriptor.setFactoryMethod(value);
		}
	}

	protected abstract Class<? extends Annotation> getFactoryAnnotation();

}
