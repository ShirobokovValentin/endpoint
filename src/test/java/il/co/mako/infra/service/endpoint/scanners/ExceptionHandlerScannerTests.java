package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.annotation.method.ExceptionHandler;
import il.co.mako.infra.service.endpoint.entity.ExceptionHandlerDescriptor;
import il.co.mako.infra.service.endpoint.scanners.DispatcherScannerTests.DefaultActionPresent;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ExceptionHandlerScannerTests extends AbstractMethodScannerTests<ExceptionHandlerScanner>
{
	@Test
	public void exHandler_errEmpty()
	{
		testException(ExHandlerEmpty.class, "Exception handler: public void il.co.mako.infra.service.endpoint.scanners.ExceptionHandlerScannerTests$ExHandlerEmpty.exceptionHandler() must have only one argument inheritor of Exception");
	}

	@Test
	public void exHandler_errNotAssignable()
	{
		testException(ExHandlerNotAssignable.class, "Exception handler: public void il.co.mako.infra.service.endpoint.scanners.ExceptionHandlerScannerTests$ExHandlerNotAssignable.exceptionHandler(java.lang.Object) must have only one argument inheritor of Exception");
	}

	@Test
	public void exHandler_errMulti()
	{
		testException(ExHandlerMulti.class, "Exception handler: public void il.co.mako.infra.service.endpoint.scanners.ExceptionHandlerScannerTests$ExHandlerMulti.exceptionHandler(java.lang.Exception,java.lang.RuntimeException) must have only one argument inheritor of Exception");
	}

	@Test
	public void exHandler()
	{
		Method method = getNecessaryMethod(ExHandlerNorm.class);
		ExceptionHandlerDescriptor descriptor = testSubject.scan(method);
		String defaultActionMethodName = descriptor.getMethod().getName();

		assertEquals("exceptionHandler", defaultActionMethodName);
	}

	// ------------------------------------------------------------

	public static class ExHandlerNorm extends DefaultActionPresent
	{
		@ExceptionHandler
		@SuppressWarnings("unused")
		public void exceptionHandler(Exception e)
		{
		}
	}

	private static class ExHandlerEmpty extends DefaultActionPresent
	{
		@ExceptionHandler
		public void exceptionHandler()
		{
		}
	}

	private static class ExHandlerNotAssignable extends DefaultActionPresent
	{
		@SuppressWarnings("unused")
		@ExceptionHandler
		public void exceptionHandler(Object o)
		{
		}
	}

	private static class ExHandlerMulti extends DefaultActionPresent
	{
		@SuppressWarnings("unused")
		@ExceptionHandler
		public void exceptionHandler(Exception e, RuntimeException r)
		{
		}
	}

	// ------------------------------------------------------------

	@Before
	public void setUp() throws Exception
	{
		testSubject = new ExceptionHandlerScanner();
	}

	@Override
	protected Method getNecessaryMethod(Class<?> clazz)
	{
		return getFirstAnnotatedMethod(clazz, ExceptionHandler.class);
	}

	protected void doTest(Method method)
	{
		testSubject.scan(method);
	}
}
