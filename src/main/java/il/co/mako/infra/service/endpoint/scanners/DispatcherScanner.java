package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.annotation.ActionInterceptor;
import il.co.mako.infra.service.endpoint.annotation.Dispatcher;
import il.co.mako.infra.service.endpoint.annotation.DispatcherInterceptor;
import il.co.mako.infra.service.endpoint.annotation.method.Action;
import il.co.mako.infra.service.endpoint.annotation.method.DefaultAction;
import il.co.mako.infra.service.endpoint.annotation.method.ExceptionHandler;
import il.co.mako.infra.service.endpoint.entity.ActionDescriptor;
import il.co.mako.infra.service.endpoint.entity.DispatcherDescriptor;
import il.co.mako.infra.service.endpoint.entity.ExceptionHandlerDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.InvalidMappingException;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;
import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

public class DispatcherScanner
{
	private final ActionScanner actionScanner = new ActionScanner();

	private final ExceptionHandlerScanner exceptionHandlerScanner = new ExceptionHandlerScanner();

	// ------------------------------------------------------------

	public DispatcherDescriptor scan(Class<?> dispatcherClass) throws InvalidMappingException
	{
		DispatcherDescriptor descriptor = new DispatcherDescriptor(dispatcherClass);

		fillActionParamName(descriptor);
		fillActions(descriptor);
		fillInterceptors(descriptor);

		return descriptor;
	}

	private void fillActionParamName(DispatcherDescriptor descriptor)
	{
		Class<?> dispatcherClass = descriptor.getDispatcherClass();
		Dispatcher annotation = dispatcherClass.getAnnotation(Dispatcher.class);

		if (annotation != null)
			descriptor.setActionParamName(annotation.value());
	}

	private void fillActions(DispatcherDescriptor descriptor)
	{
		Class<?> dispatcherClass = descriptor.getDispatcherClass();
		Method[] methods = dispatcherClass.getMethods();

		resolveDefaultAction(descriptor, methods);

		for (Method method : methods)
		{
			resolveAction(descriptor, method);
			resolveExceptionHandler(descriptor, method);
		}

	}

	private void fillInterceptors(DispatcherDescriptor descriptor)
	{
		Class<?> dispatcherClass = descriptor.getDispatcherClass();
		resolveDispatcherInterceptor(descriptor, dispatcherClass);
		resolveActionInterceptor(descriptor, dispatcherClass);
	}

	private void resolveDispatcherInterceptor(DispatcherDescriptor descriptor, Class<?> dispatcherClass)
	{
		DispatcherInterceptor annotation = dispatcherClass.getAnnotation(DispatcherInterceptor.class);
		if (annotation == null)
			return;

		Class<? extends AroundDispaching>[] interceptorsClasses = annotation.value();

		addDispachingInterceptors(descriptor, interceptorsClasses);
	}

	private void addDispachingInterceptors(DispatcherDescriptor descriptor, Class<? extends AroundDispaching>[] interceptorsClasses)
	{
		for (Class<? extends AroundDispaching> interceptorsClass : interceptorsClasses)
		{
			try
			{
				descriptor.addDispachingInterceptors(interceptorsClass.newInstance());
			} catch (Exception e)
			{
				throw new InvalidMappingException("Can't create interceptor " + interceptorsClass, e);
			}
		}
	}

	private void resolveActionInterceptor(DispatcherDescriptor descriptor, Class<?> dispatcherClass)
	{
		ActionInterceptor annotation = dispatcherClass.getAnnotation(ActionInterceptor.class);
		if (annotation == null)
			return;
		Class<? extends AroundAction>[] interceptorsClasses = annotation.value();

		addActionInterceptors(descriptor, interceptorsClasses);
	}

	private void addActionInterceptors(DispatcherDescriptor descriptor, Class<? extends AroundAction>[] interceptorsClasses)
	{
		for (Class<? extends AroundAction> interceptorsClass : interceptorsClasses)
		{
			try
			{
				descriptor.addActionInterceptors(interceptorsClass.newInstance());
			} catch (Exception e)
			{
				throw new InvalidMappingException("Can't create interceptor " + interceptorsClass, e);
			}
		}
	}

	private void resolveDefaultAction(DispatcherDescriptor descriptor, Method[] methods)
	{
		ActionDescriptor defaultActionDescriptor = null;
		for (Method method : methods)
		{
			if (!method.isAnnotationPresent(DefaultAction.class))
				continue;

			if (defaultActionDescriptor != null)
				throw new InvalidMappingException("DefaultAction annotation is not unique");

			defaultActionDescriptor = actionScanner.scan(method);
		}

		if (defaultActionDescriptor == null)
			throw new InvalidMappingException("DefaultAction annotation is not present");

		descriptor.setDefaultAction(defaultActionDescriptor);
	}

	private void resolveAction(DispatcherDescriptor descriptor, Method method)
	{
		if (!method.isAnnotationPresent(Action.class))
			return;

		String[] actionKeys = method.getAnnotation(Action.class).value();

		ActionDescriptor aDescriptor = actionScanner.scan(method);

		for (String actionKey : actionKeys)
		{
			if (StringUtils.isEmpty(actionKey))
				throw new InvalidMappingException("Action key is empty");

			descriptor.putAction(actionKey, aDescriptor);
		}
	}

	private void resolveExceptionHandler(DispatcherDescriptor descriptor, Method method)
	{
		if (!method.isAnnotationPresent(ExceptionHandler.class))
			return;

		ExceptionHandlerDescriptor exHandlerDescriptor = exceptionHandlerScanner.scan(method);

		Class<? extends Exception> key = exHandlerDescriptor.getArgumentType();

		if (descriptor.getExceptionHandler(key) != null)
			throw new InvalidMappingException("ExceptionHandler for " + key + " is not unique");

		descriptor.putExceptionHandler(key, exHandlerDescriptor);
	}

}
