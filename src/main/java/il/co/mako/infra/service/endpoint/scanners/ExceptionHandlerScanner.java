package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.entity.ExceptionHandlerDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.InvalidMappingException;

import java.lang.reflect.Method;

public class ExceptionHandlerScanner extends AbstractMethodScanner
{
	public ExceptionHandlerDescriptor scan(Method method)
	{
		ExceptionHandlerDescriptor descriptor = new ExceptionHandlerDescriptor(method);

		processArgument(descriptor);
		processReturnType(descriptor);

		return descriptor;
	}

	private void processArgument(ExceptionHandlerDescriptor descriptor)
	{
		Method method = descriptor.getMethod();
		Class<?>[] argumentTypes = method.getParameterTypes();

		if (argumentTypes.length == 1 && Exception.class.isAssignableFrom(argumentTypes[0]))
		{
			@SuppressWarnings("unchecked")
			Class<? extends Exception> type = (Class<? extends Exception>) argumentTypes[0];
			descriptor.setArgumentType(type);
			return;
		}

		throw new InvalidMappingException("Exception handler: " + method + " must have only one argument inheritor of Exception");
	}
	
}
