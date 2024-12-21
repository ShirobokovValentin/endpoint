package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.MediaType;
import il.co.mako.infra.service.endpoint.annotation.ActionInterceptor;
import il.co.mako.infra.service.endpoint.annotation.method.Action;
import il.co.mako.infra.service.endpoint.annotation.method.Representation;
import il.co.mako.infra.service.endpoint.annotation.parameter.RequestParam;
import il.co.mako.infra.service.endpoint.entity.ActionDescriptor;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;
import il.co.mako.infra.service.endpoint.scanners.DispatcherScannerTests.DefaultActionPresent;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActionScannerTests extends AbstractMethodScannerTests<ActionScanner>
{
	@Test
	public void returnType_void()
	{
		Method method = getNecessaryMethod(ActionNormVoid.class);
		ActionDescriptor descriptor = testSubject.scan(method);

		assertEquals(void.class, descriptor.getReturnType());
	}

	@Test
	public void complex()
	{
		Method method = getNecessaryMethod(ActionComplex.class);
		ActionDescriptor descriptor = testSubject.scan(method);

		assertEquals(String.class, descriptor.getReturnType());
		assertEquals(MediaType.APPLICATION_XML, descriptor.getMediaType());
		assertEquals(Interceptor.class, descriptor.getInterceptors().get(0).getClass());
	}

	@Test
	public void convertorNotSupported()
	{
		testException(ActionArgNotSupported.class, "Can't create convertor for class: Object");
	}

	@Test
	public void interceptor_fail()
	{
		testException(ActionInterceptorFail.class, "Can't create interceptor class il.co.mako.infra.service.endpoint.scanners.ActionScannerTests$FailInterceptor");
	}

	@Test
	public void paramFail()
	{
		testException(ActionParamFail.class,
				"In method \"il.co.mako.infra.service.endpoint.scanners.ActionScannerTests.ActionParamFail.action(...) annotation [@ParamReaderClass, @AttributeReaderClass] for argument 1 not found");
	}

	// ------------------------------------------------------------

	@Before
	public void setUp()
	{
		testSubject = new ActionScanner();
	}

	@Override
	protected Method getNecessaryMethod(Class<?> clazz)
	{
		return getFirstAnnotatedMethod(clazz, Action.class);
	}

	protected void doTest(Method method)
	{
		testSubject.scan(method);
	}

	// ------------------------------------------------------------

	private static class ActionNormVoid extends DefaultActionPresent
	{
		@Action("aKey")
		@SuppressWarnings("unused")
		public void action()
		{
		}
	}

	private static class ActionComplex extends DefaultActionPresent
	{
		@Action("aKey")
		@Representation(MediaType.APPLICATION_XML)
		@ActionInterceptor(Interceptor.class)
		@SuppressWarnings("unused")
		public String action(@RequestParam("p") String o)
		{
			return o;
		}
	}

	private static class ActionArgNotSupported extends DefaultActionPresent
	{
		@Action("aKey")
		@SuppressWarnings("unused")
		public String action(@RequestParam("p") Object o)
		{
			return o.toString();
		}
	}

	private static class ActionInterceptorFail extends DefaultActionPresent
	{
		@Action("aKey")
		@ActionInterceptor(FailInterceptor.class)
		@SuppressWarnings("unused")
		public void action()
		{
		}
	}

	private static class ActionParamFail extends DefaultActionPresent
	{
		@Action("aKey")
		@SuppressWarnings("unused")
		public void action(Object o)
		{
		}
	}

	// ------------------------------------------------------------

	static class Interceptor implements AroundAction
	{

		public void beforeMethod(Method method, List<Object> arguments)
		{
		}

		public Object afterMethod(Object result)
		{
			return null;
		}

		public void beforeExceptionHandling(Method method, Throwable e)
		{
		}

		public Object afterExceptionHandling(Object result)
		{
			return null;
		}

	}

	static class FailInterceptor extends Interceptor
	{
		private FailInterceptor()
		{
		}
	}

}
