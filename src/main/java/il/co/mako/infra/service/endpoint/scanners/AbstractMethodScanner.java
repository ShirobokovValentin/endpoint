package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.entity.AbstractMethodDescriptor;

import java.lang.reflect.Method;

public abstract class AbstractMethodScanner
{
	protected void processReturnType(AbstractMethodDescriptor descriptor)
	{
		Method method = descriptor.getMethod();
		Class<?> returnType = method.getReturnType();
		descriptor.setReturnType(returnType);
	}
	
}
