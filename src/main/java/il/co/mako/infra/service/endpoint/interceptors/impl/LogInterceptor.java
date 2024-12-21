package il.co.mako.infra.service.endpoint.interceptors.impl;

import il.co.mako.infra.service.MakoReflectionUtils;
import il.co.mako.infra.service.endpoint.convertors.PossiblyHebrewConvertor;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;
import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;
import il.co.mako.infra.service.endpoint.readers.IpReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;

public class LogInterceptor implements AroundAction, AroundDispaching
{
	private static final String BOUNDARY_START = "--------------------------------------------------";

	private static final String BOUNDARY_FINISH = "==================================================";

	private final Logger  LOG;

	public LogInterceptor(Class<?> clazz)
	{
		this.LOG = LogManager.getLogger(clazz);
	}

	public LogInterceptor(String clazz)
	{
		this.LOG = LogManager.getLogger(clazz);
	}

	// ------------------------------------------------------------

	public HttpServletRequest beforeExtraction(HttpServletRequest request)
	{
		String ip = IpReader.getInstance().read(request);
		String characterEncoding = request.getCharacterEncoding();
		if (characterEncoding == null)
			characterEncoding = "default";
		PossiblyHebrewConvertor convertor = PossiblyHebrewConvertor.getInstance();
		@SuppressWarnings("unchecked")
		Enumeration<String> params = request.getParameterNames();

		LOG.info(BOUNDARY_START);
		LOG.info("User with ip: " + ip + " initialized the request");
		LOG.info("Query String: " + getQueryString(request));
		LOG.debug("CharacterEncoding: " + characterEncoding);


		while (params.hasMoreElements())
		{
			String paramName = params.nextElement();
			String paramValue = request.getParameter(paramName);
			LOG.info("\t" + paramName + " = " + convertor.fromString(paramValue));
		}

		return request;
	}

	private String getQueryString(HttpServletRequest request)
	{
		String queryString = request.getQueryString();

		if (queryString == null)
		{
			StringBuilder sb = new StringBuilder();
			@SuppressWarnings("unchecked")
			Enumeration<String> params = request.getParameterNames();
			boolean first = true;
			while (params.hasMoreElements())
			{
				if (!first)
					sb.append("&");
				else
					first = false;
				String paramName = params.nextElement();
				String paramValue = request.getParameter(paramName);
				sb.append(paramName);
				sb.append("=");
				sb.append(paramValue);
			}

			sb.append(" (synthetic)");
			queryString = sb.toString();
		}

		return queryString;
	}

	public void beforeMethod(Method method, List<Object> arguments)
	{
		LOG.info("Method: " + MakoReflectionUtils.prettyMethod(method) + " was called");
	}

	public Object afterMethod(Object result)
	{
		LOG.info("Method return: " + (result == null ? null : result.toString()));
		LOG.info(BOUNDARY_FINISH);

		return result;
	}

	public void beforeExceptionHandling(Method method, Throwable e)
	{
		LOG.info("Exception : " + e.getClass().getSimpleName() + " was thrown.");
		LOG.info("Exception Handler: " + MakoReflectionUtils.fullMethod(method) + " was called");
	}

	public Object afterExceptionHandling(Object result)
	{
		LOG.info("Exception Handler return: " + (result == null ? null : result.toString()));
		LOG.info(BOUNDARY_FINISH);
		return result;
	}

	public String afterConverting(String result)
	{
		return result;
	}

}
