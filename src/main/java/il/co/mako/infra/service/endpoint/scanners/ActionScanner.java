package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.MediaType;
import il.co.mako.infra.service.endpoint.annotation.ActionInterceptor;
import il.co.mako.infra.service.endpoint.annotation.method.Representation;
import il.co.mako.infra.service.endpoint.entity.ActionDescriptor;
import il.co.mako.infra.service.endpoint.entity.ArgumentDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.InvalidMappingException;
import il.co.mako.infra.service.endpoint.exceptions.RequredAnnotationMissingException;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActionScanner extends AbstractMethodScanner
{
	private final ArgumentScanner argumentScanner = new ArgumentScanner();

	// ------------------------------------------------------------

	public ActionDescriptor scan(Method method)
	{
		ActionDescriptor actionDescriptor = new ActionDescriptor(method);

		processReturnType(actionDescriptor);
		processMediaType(actionDescriptor);
		processInterceptor(actionDescriptor);
		processArguments(actionDescriptor);

		return actionDescriptor;
	}

	// ------------------------------------------------------------

	private void processMediaType(ActionDescriptor actionDescriptor)
	{
		Method method = actionDescriptor.getMethod();
		if (method.isAnnotationPresent(Representation.class))
		{
			MediaType mediaType = method.getAnnotation(Representation.class).value();
			actionDescriptor.setMediaType(mediaType);
		}
	}

	private void processInterceptor(ActionDescriptor actionDescriptor)
	{
		Method method = actionDescriptor.getMethod();
		if (method.isAnnotationPresent(ActionInterceptor.class))
		{
			Class<? extends AroundAction>[] interceptorsClasses = method.getAnnotation(ActionInterceptor.class).value();
			addInterceptors(actionDescriptor, interceptorsClasses);
		}
	}

	private void addInterceptors(ActionDescriptor actionDescriptor, Class<? extends AroundAction>[] interceptorsClasses)
	{
		for (Class<? extends AroundAction> interceptorsClass : interceptorsClasses)
		{
			try
			{
				actionDescriptor.addInterceptor(interceptorsClass.newInstance());
			} catch (Exception e)
			{
				throw new InvalidMappingException("Can't create interceptor " + interceptorsClass, e);
			}
		}
	}

	private void processArguments(ActionDescriptor actionDescriptor) throws InvalidMappingException
	{
		Method method = actionDescriptor.getMethod();
		Class<?>[] argumentTypes = method.getParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();

		List<ArgumentDescriptor> argDescriptors = new ArrayList<ArgumentDescriptor>();
		for (int i = 0; i < argumentTypes.length; i++)
		{
			try
			{
				ArgumentDescriptor argDescriptor = argumentScanner.scan(annotations[i], argumentTypes[i]);
				argDescriptors.add(argDescriptor);
			} catch (RequredAnnotationMissingException e)
			{
				throw new InvalidMappingException("In method \"" + method.getDeclaringClass().getCanonicalName() + "." + method.getName() +
						"(...) annotation " + e.getAnnotations() + " for argument " + (i + 1) + " not found");
			}
		}

		actionDescriptor.setArgumentDescriptors(argDescriptors);
	}

}
