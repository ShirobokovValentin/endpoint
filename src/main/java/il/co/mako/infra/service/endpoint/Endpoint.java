package il.co.mako.infra.service.endpoint;

import il.co.mako.infra.service.convertors.ConvertorFactory;
import il.co.mako.infra.service.convertors.ConvertorService;
import il.co.mako.infra.service.endpoint.constraint.entity.Conclusion;
import il.co.mako.infra.service.endpoint.constraint.exceptions.ArgumentValidationException;
import il.co.mako.infra.service.endpoint.constraint.exceptions.ViolationException;
import il.co.mako.infra.service.endpoint.entity.ActionDescriptor;
import il.co.mako.infra.service.endpoint.entity.ArgumentDescriptor;
import il.co.mako.infra.service.endpoint.entity.DispatcherDescriptor;
import il.co.mako.infra.service.endpoint.entity.ExceptionHandlerDescriptor;
import il.co.mako.infra.service.endpoint.exceptions.EndpointException;
import il.co.mako.infra.service.endpoint.exceptions.InvalidMappingException;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;
import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;
import il.co.mako.infra.service.endpoint.scanners.DispatcherScanner;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Endpoint
{
	private final Object dispatcher;

	private final DispatcherDescriptor dispatcherDescriptor;

	// ------------------------------------------------------------

	private Endpoint(Object dispatcher, DispatcherDescriptor dispatcherDescriptor)
	{
		this.dispatcher = dispatcher;
		this.dispatcherDescriptor = dispatcherDescriptor;
	}

	public static Endpoint newInstance(Object dispatcher) throws InvalidMappingException
	{
		DispatcherScanner dispatcherScanner = new DispatcherScanner();
		DispatcherDescriptor dispatcherDescriptor = dispatcherScanner.scan(dispatcher.getClass());
		return new Endpoint(dispatcher, dispatcherDescriptor);
	}

	// ------------------------------------------------------------

	public String process(HttpServletRequest request)
	{
		try
		{
			return tryProcess(request);

		} catch (EndpointException e)
		{
			throw e;
		} catch (Error e)
		{
			throw e;
		} catch (Throwable e)
		{
			throw new EndpointException(e);
		}
	}

	private String tryProcess(HttpServletRequest request) throws Throwable
	{
		HttpServletRequest modifiedRequest = applyBeforeExtractionInterceptors(request);

		String actionParamName = dispatcherDescriptor.getActionParamName();
		String actionValue = actionParamName != null ? modifiedRequest.getParameter(actionParamName) : null;

		ActionDescriptor actionDescriptor = dispatcherDescriptor.getAction(actionValue);
		List<ArgumentDescriptor> paramDescriptors = actionDescriptor.getArgumentDescriptors();

		Method method = actionDescriptor.getMethod();
		Object invocationResult;
		try
		{
			List<Object> arguments = extractArguments(paramDescriptors, modifiedRequest);
			applyBeforeMethodInterceptors(method, arguments, actionDescriptor);
			invocationResult = method.invoke(dispatcher, arguments.toArray());
			invocationResult = applyAfterMethodInterceptors(invocationResult, actionDescriptor);
		} catch (InvocationTargetException ite)
		{
			Throwable e = ite.getCause();
			invocationResult = handlException(e, actionDescriptor);
		} catch (Exception e)
		{
			invocationResult = handlException(e, actionDescriptor);
		}

		ConvertorService convertorService = ConvertorFactory.getConvertorService(actionDescriptor.getMediaType());
		String result = convertorService.convert(invocationResult);
		String modifiedResult = applyAfterConvertingInterceptors(result);

		return modifiedResult;
	}

	private Object handlException(Throwable e, ActionDescriptor actionDescriptor) throws Throwable
	{
		ExceptionHandlerDescriptor exceptionHandler = dispatcherDescriptor.getSuitableExceptionHandler(e);
		if (exceptionHandler == null)
			throw e;

		Method handlerMethod = exceptionHandler.getMethod();
		applyBeforeExceptionHandling(handlerMethod, e, actionDescriptor);
		Object invocationResult = handlerMethod.invoke(dispatcher, e);
		invocationResult = applyAfterExceptionHandling(invocationResult, actionDescriptor);
		return invocationResult;
	}

	private HttpServletRequest applyBeforeExtractionInterceptors(HttpServletRequest request)
	{
		List<AroundDispaching> interceptors = dispatcherDescriptor.getDispachingInterceptors();

		HttpServletRequest result = request;
		for (AroundDispaching interceptor : interceptors)
		{
			result = interceptor.beforeExtraction(result);
		}
		return request;
	}

	private void applyBeforeMethodInterceptors(Method method, List<Object> arguments, ActionDescriptor actionDescriptor)
	{
		for (AroundAction interceptor : findInterceptors(actionDescriptor))
		{
			interceptor.beforeMethod(method, arguments);
		}
	}

	private Object applyAfterMethodInterceptors(Object obj, ActionDescriptor actionDescriptor)
	{
		Object result = obj;
		for (AroundAction interceptor : findInterceptors(actionDescriptor))
		{
			result = interceptor.afterMethod(result);
		}
		return result;
	}

	private void applyBeforeExceptionHandling(Method method, Throwable arguments, ActionDescriptor actionDescriptor)
	{
		for (AroundAction interceptor : findInterceptors(actionDescriptor))
		{
			interceptor.beforeExceptionHandling(method, arguments);
		}
	}

	private Object applyAfterExceptionHandling(Object obj, ActionDescriptor actionDescriptor)
	{
		Object result = obj;
		for (AroundAction interceptor : findInterceptors(actionDescriptor))
		{
			result = interceptor.afterExceptionHandling(result);
		}
		return result;
	}

	private List<AroundAction> findInterceptors(ActionDescriptor actionDescriptor)
	{
		if (actionDescriptor.getInterceptors() != null)
			return actionDescriptor.getInterceptors();

		return dispatcherDescriptor.getActionInterceptors();
	}

	private String applyAfterConvertingInterceptors(String result)
	{
		List<AroundDispaching> interceptors = dispatcherDescriptor.getDispachingInterceptors();

		String newResult = result;
		for (AroundDispaching interceptor : interceptors)
		{
			newResult = interceptor.afterConverting(newResult);
		}
		return newResult;
	}

	private List<Object> extractArguments(List<ArgumentDescriptor> paramDescriptors, HttpServletRequest request) throws ArgumentValidationException
	{
		List<Object> arguments = new ArrayList<Object>();
		Conclusion conclusion = new Conclusion();

		for (ArgumentDescriptor argumentDescriptor : paramDescriptors)
		{
			try
			{
				Object valueFromRequest = argumentDescriptor.getArgument(request);
				arguments.add(valueFromRequest);
			} catch (ViolationException e)
			{
				conclusion.put(e.getParamName(), e.getViolation());
			}
		}

		if (conclusion.hasViolation())
			throw new ArgumentValidationException(conclusion);

		return arguments;
	}

}
