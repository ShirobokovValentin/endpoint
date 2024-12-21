package il.co.mako.infra.service.endpoint;

import il.co.mako.infra.service.endpoint.annotation.ActionInterceptor;
import il.co.mako.infra.service.endpoint.annotation.Dispatcher;
import il.co.mako.infra.service.endpoint.annotation.DispatcherInterceptor;
import il.co.mako.infra.service.endpoint.annotation.method.Action;
import il.co.mako.infra.service.endpoint.annotation.method.DefaultAction;
import il.co.mako.infra.service.endpoint.annotation.method.ExceptionHandler;
import il.co.mako.infra.service.endpoint.annotation.parameter.RequestParam;
import il.co.mako.infra.service.endpoint.constraint.annotation.NotNull;
import il.co.mako.infra.service.endpoint.constraint.exceptions.ArgumentValidationException;
import il.co.mako.infra.service.endpoint.exceptions.EndpointException;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;
import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;
import il.co.mako.infra.service.AbstractTests;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class EndpointIntegrationTests extends AbstractTests<Endpoint>
{

	private static final String ACTION_PARAM_NAME = "action";

	private static final String DEFAULT_ACTION_RESULT = "DEFAULT_ACTION_RESULT";

	private static final String OTHER_KEY = "OTHER_KEY";

	private static final String OTHER_ACTION_RESULT = "OTHER_ACTION_RESULT";

	private static final String VOID_KEY = "VOID_KEY";

	// ------------------------------------------------------------

	private static final String PRIMITIVE_KEY = "PRIMITIVE_KEY";

	private static final String OBJECTS_KEY = "OBJECTS_KEY";

	private static final String REQUEST_KEY = "REQUEST_KEY";

	private static final String ENUM_KEY = "ENUM_KEY";

	// ------------------------------------------------------------

	private static final String A_INTERCEPTOR_KEY = "action interceptor";

	private static final String A_INTERCEPTOR_INPUT = "input ";

	private static final String A_INTERCEPTOR_DELTA_AFTER = "after";

	private static final String A_INTERCEPTOR_DELTA_BEFORE = "before ";

	// ------------------------------------------------------------

	private static final String EX_KEY = "EX_KEY";

	private static final String EX_INPUT = "EX_INPUT ";

	private static final String EX_HANDLING = "EX_HANDLING";

	private static final String EX_INTERCEPTOR_DELTA = "EX_INTERCEPTOR_DELTA";

	private static boolean beforeExceptionHandlingWasCalled = false;

	// ------------------------------------------------------------

	private static final String D_INTERCEPTOR_KEY = "D_INTERCEPTOR_KEY";

	private static final String D_INTERCEPTOR_PARAM_NAME = "D_INTERCEPTOR_PARAM_NAME";

	private static final String D_INTERCEPTOR_PARAM_VALUE = "D_INTERCEPTOR_PARAM_VALUE";

	private static final String D_INTERCEPTOR_DELTA = "D_INTERCEPTOR_DELTA";

	// ------------------------------------------------------------

	private static final String VALIDATOR_KEY = "VALIDATOR_KEY";

	private static final String VALIDATOR_HANDLER_RESULT = "VALIDATOR_HANDLER_RESULT";

	// ------------------------------------------------------------

	private static final String ERROR_KEY = "ERROR_KEY";

	private static final String THROWABLE_KEY = "THROWABLE_KEY";

	// ------------------------------------------------------------

	protected MockHttpServletRequest request = new MockHttpServletRequest();

	// ------------------------------------------------------------

	@Before
	public void setUp() throws Exception
	{
		testSubject = Endpoint.newInstance(new DispatcherClass());
	}

	@Test
	public void defaultAction()
	{
		String actual = testSubject.process(request);

		assertEquals(DEFAULT_ACTION_RESULT, actual);
	}

	@Test
	public void otherAction()
	{
		request.setParameter(ACTION_PARAM_NAME, OTHER_KEY);

		String actual = testSubject.process(request);

		assertEquals(OTHER_ACTION_RESULT, actual);
	}

	@Test
	public void voidMethod()
	{
		request.setParameter(ACTION_PARAM_NAME, VOID_KEY);

		String actual = testSubject.process(request);

		assertNull(actual);
	}

	@Test
	public void emptyRequest()
	{
		testSubject = Endpoint.newInstance(new EmptyDispatcher());
		String actual = testSubject.process(request);

		assertEquals(DEFAULT_ACTION_RESULT, actual);
	}

	// ------------------------------------------------------------

	@Test
	public void primitives_normal()
	{
		request.setParameter("b", "true");
		request.setParameter("bt", "1");
		request.setParameter("c", "c");
		request.setParameter("s", "11");
		request.setParameter("i", "101");
		request.setParameter("l", "1001");
		request.setParameter("f", "1.01");
		request.setParameter("d", "10.01");

		request.setParameter(ACTION_PARAM_NAME, PRIMITIVE_KEY);

		String actual = testSubject.process(request);

		String expected = "{\"b\":true,\"bt\":1,\"c\":\"c\",\"s\":11,\"i\":101,\"l\":1001,\"f\":1.01,\"d\":10.01}";

		assertEquals(expected, actual);
	}

	@Test
	public void primitive_null()
	{
		request.setParameter(ACTION_PARAM_NAME, PRIMITIVE_KEY);

		String actual = getExceptionMessage();
		String expected =
				  "Parameter b: Cannot get boolean from \"null\"; "
				+ "Parameter bt: Cannot get byte from \"null\"; "
				+ "Parameter c: Cannot get char from \"null\"; "
				+ "Parameter s: Cannot get short from \"null\"; "
				+ "Parameter d: Cannot get double from \"null\"; "
				+ "Parameter f: Cannot get float from \"null\"; "
				+ "Parameter i: Cannot get int from \"null\"; "
				+ "Parameter l: Cannot get long from \"null\"";

		assertEquals(expected, actual);
	}

	@Test
	public void primitive_err()
	{
		fillErrorValues();

		request.setParameter(ACTION_PARAM_NAME, PRIMITIVE_KEY);

		String actual = getExceptionMessage();

		String expected =
				  "Parameter bt: Cannot get byte from \"byte\"; "
				+ "Parameter c: Cannot get char from \"char\"; "
				+ "Parameter s: Cannot get short from \"short\"; "
				+ "Parameter d: Cannot get double from \"double\"; "
				+ "Parameter f: Cannot get float from \"float\"; "
				+ "Parameter i: Cannot get int from \"int\"; "
				+ "Parameter l: Cannot get long from \"long\""

		;

		assertEquals(expected, actual);
	}

	@Test
	public void objects_normal()
	{
		request.setParameter("b", "true");
		request.setParameter("bt", "2");
		request.setParameter("c", "C");
		request.setParameter("s", "12");
		request.setParameter("i", "102");
		request.setParameter("l", "1002");
		request.setParameter("f", "1.02");
		request.setParameter("d", "10.02");
		request.setParameter("st", "str");
		request.setParameter("dt", "427705200000");

		request.setParameter(ACTION_PARAM_NAME, OBJECTS_KEY);

		String actual = testSubject.process(request);

		String expected = "{\"b\":true,\"bt\":2,\"c\":\"C\",\"s\":12,\"i\":102,\"l\":1002,\"f\":1.02,\"d\":10.02,\"st\":\"str\",\"dt\":427705200000}";

		assertEquals(expected, actual);
	}

	@Test
	public void objects_null()
	{
		request.setParameter(ACTION_PARAM_NAME, OBJECTS_KEY);

		String actual = testSubject.process(request);
		String expected = "{}";

		assertEquals(expected, actual);
	}

	@Test
	public void objects_err()
	{
		fillErrorValues();
		request.setParameter("st", "string");
		request.setParameter("dt", "date");

		request.setParameter(ACTION_PARAM_NAME, OBJECTS_KEY);

		String actual = getExceptionMessage();

		String expected =
				  "Parameter dt: Cannot get Date from \"date\"; "
				+ "Parameter bt: Cannot get Byte from \"byte\"; "
				+ "Parameter c: Cannot get Character from \"char\"; "
				+ "Parameter s: Cannot get Short from \"short\"; "
				+ "Parameter d: Cannot get Double from \"double\"; "
				+ "Parameter f: Cannot get Float from \"float\"; "
				+ "Parameter i: Cannot get Integer from \"int\"; "
				+ "Parameter l: Cannot get Long from \"long\"";

		assertEquals(expected, actual);
	}

	// ------------------------------------------------------------

	@Test
	public void request()
	{
		request.setParameter(ACTION_PARAM_NAME, REQUEST_KEY);

		Object actual = testSubject.process(request);
		String expected = "\"org.springframework.mock.web.MockHttpServletRequest\"";

		assertEquals(expected, actual);
	}

	// ------------------------------------------------------------

	@Test
	public void enums_normal()
	{
		request.setParameter(ACTION_PARAM_NAME, ENUM_KEY);
		request.setParameter("e", "EL1");

		Object actual = testSubject.process(request);
		String expected = "\"EL1\"";

		assertEquals(expected, actual);
	}

	@Test
	public void enums_null()
	{
		request.setParameter(ACTION_PARAM_NAME, ENUM_KEY);
		Object actual = testSubject.process(request);
		assertNull(actual);
	}

	@Test
	public void enums_error()
	{
		request.setParameter(ACTION_PARAM_NAME, ENUM_KEY);
		request.setParameter("e", "EL3");
		String actual = getExceptionMessage();
		assertEquals("Parameter e: Cannot get " + MyEnum.class.getSimpleName() + " from \"EL3\"", actual);
	}

	// ------------------------------------------------------------
	@Test
	public void aInterceptor_befor_after()
	{
		request.setParameter(ACTION_PARAM_NAME, A_INTERCEPTOR_KEY);

		String actual = testSubject.process(request);

		String expected = A_INTERCEPTOR_INPUT + A_INTERCEPTOR_DELTA_BEFORE + A_INTERCEPTOR_DELTA_AFTER;
		assertEquals(expected, actual);
	}

	@Test
	public void aInterceptor_exeptions()
	{
		request.setParameter(ACTION_PARAM_NAME, EX_KEY);

		String actual = testSubject.process(request);

		String expected = EX_INPUT + EX_HANDLING + EX_INTERCEPTOR_DELTA;
		assertEquals(expected, actual);
		assertTrue(beforeExceptionHandlingWasCalled);
	}

	// ------------------------------------------------------------

	@Test
	public void dInterceptor()
	{
		request.setParameter(ACTION_PARAM_NAME, D_INTERCEPTOR_KEY);

		String actual = testSubject.process(request);

		String expected = D_INTERCEPTOR_PARAM_VALUE + D_INTERCEPTOR_DELTA;
		assertEquals(expected, actual);
	}

	// ------------------------------------------------------------

	@Test
	public void simpleValidation()
	{
		testSubject = Endpoint.newInstance(new DispatcherClassWithHandler());
		request.setParameter(ACTION_PARAM_NAME, VALIDATOR_KEY);

		String actual = getExceptionMessage();

        assertEquals(VALIDATOR_HANDLER_RESULT, actual);
	}

	// ------------------------------------------------------------

	@Test(expected = Error.class)
	public void throwError()
	{
		request.setParameter(ACTION_PARAM_NAME, ERROR_KEY);

		testSubject.process(request);
	}

	@Test(expected = EndpointException.class)
	public void throwOther()
	{
		request.setParameter(ACTION_PARAM_NAME, THROWABLE_KEY);
		testSubject.process(request);
	}

	// ------------------------------------------------------------

	private void fillErrorValues()
	{
		request.setParameter("b", "boolen");
		request.setParameter("bt", "byte");
		request.setParameter("c", "char");
		request.setParameter("s", "short");
		request.setParameter("i", "int");
		request.setParameter("l", "long");
		request.setParameter("f", "float");
		request.setParameter("d", "double");
	}

	private String getExceptionMessage()
	{
		try
		{
			return testSubject.process(request);
		} catch (Exception e)
		{
			return e.getMessage();
		}
		//return null;
	}

	// ------------------------------------------------------------

	private static class EmptyDispatcher
	{
		@DefaultAction
		public String defaultAction()
		{
			return DEFAULT_ACTION_RESULT;
		}
	}

	@Dispatcher(ACTION_PARAM_NAME)
	@DispatcherInterceptor(DInterceptor.class)
	private static class DispatcherClass extends EmptyDispatcher
	{
		@Action(OTHER_KEY)
		public String otheAction()
		{
			return OTHER_ACTION_RESULT;
		}

		@Action(VOID_KEY)
		public void voidAction()
		{
		}

		@Action(PRIMITIVE_KEY)
		public Primitives primitive(
				@RequestParam("b") boolean b,
				@RequestParam("bt") byte bt,
				@RequestParam("c") char c,
				@RequestParam("s") short s,
				@RequestParam("i") int i,
				@RequestParam("l") long l,
				@RequestParam("f") float f,
				@RequestParam("d") double d)
		{
			return new Primitives(b, bt, c, s, i, l, f, d);
		}

		@Action(OBJECTS_KEY)
		public Objects objects(
				@RequestParam("b") Boolean b,
				@RequestParam("bt") Byte bt,
				@RequestParam("c") Character c,
				@RequestParam("s") Short s,
				@RequestParam("i") Integer i,
				@RequestParam("l") Long l,
				@RequestParam("f") Float f,
				@RequestParam("d") Double d,
				@RequestParam("st") String st,
				@RequestParam("dt") Date dt)
		{
			return new Objects(b, bt, c, s, i, l, f, d, st, dt);
		}

		@Action(REQUEST_KEY)
		public Class<?> request(
				HttpServletRequest request)
		{
			return request.getClass();
		}

		@Action(ENUM_KEY)
		public MyEnum enums(
				@RequestParam("e") MyEnum request)
		{
			return request;
		}

		@Action("errors")
		public Primitives errors(
				@RequestParam("b") boolean b,
				@RequestParam("bt") byte bt,
				@RequestParam("c") char c,
				@RequestParam("s") short s,
				@RequestParam("i") int i,
				@RequestParam("l") long l,
				@RequestParam("f") float f,
				@RequestParam("d") double d)
		{
			return new Primitives(b, bt, c, s, i, l, f, d);
		}

		@Action(A_INTERCEPTOR_KEY)
		@ActionInterceptor(AInterceptor.class)
		public String interceptor(@RequestParam(value = "p", defaultValue = A_INTERCEPTOR_INPUT) String s)
		{
			return s;
		}

		@SuppressWarnings("unused")
		@Action(EX_KEY)
		@ActionInterceptor(AInterceptor.class)
		public String interceptor_ex(@RequestParam("p") String s)
		{
			throw new TestEx(EX_INPUT);
		}

		@Action(D_INTERCEPTOR_KEY)
		public String d_interceptor(@RequestParam(D_INTERCEPTOR_PARAM_NAME) String s)
		{
			return s;
		}

		@Action(VALIDATOR_KEY)
		public void validator(@SuppressWarnings("unused") @RequestParam("p") @NotNull String s)
		{
		}

		@Action(ERROR_KEY)
		public void err()
		{
			throw new Error();
		}

		@Action(THROWABLE_KEY)
		public void throwable()
		{
			throw new IllegalArgumentException();
		}

		@ExceptionHandler
		public String exceptionHandler(TestEx e)
		{
			return e.getMessage() + EX_HANDLING;
		}

	}

	class DispatcherClassWithHandler extends DispatcherClass
	{
		@ExceptionHandler
		public String argumentValidationHandler(@SuppressWarnings("unused") ArgumentValidationException e)
		{
			return VALIDATOR_HANDLER_RESULT;
		}
	}

	static class Objects
	{
		@XmlElement
		private Boolean b;

		@XmlElement
		private Byte bt;

		@XmlElement
		private Character c;

		@XmlElement
		private Short s;

		@XmlElement
		private Integer i;

		@XmlElement
		private Long l;

		@XmlElement
		private Float f;

		@XmlElement
		private Double d;

		@XmlElement
		private String st;

		@XmlElement
		private Date dt;

		public Objects(Boolean b, Byte bt, Character c, Short s, Integer i, Long l, Float f, Double d, String st, Date dt)
		{
			this.b = b;
			this.bt = bt;
			this.c = c;
			this.s = s;
			this.i = i;
			this.l = l;
			this.f = f;
			this.d = d;
			this.st = st;
			this.dt = dt;
		}

	}

	static class Primitives
	{
		@XmlElement
		private boolean b;

		@XmlElement
		private byte bt;

		@XmlElement
		private char c;

		@XmlElement
		private short s;

		@XmlElement
		private int i;

		@XmlElement
		private long l;

		@XmlElement
		private float f;

		@XmlElement
		private double d;

		public Primitives(boolean b, byte bt, char c, short s, int i, long l, float f, double d)
		{
			this.b = b;
			this.bt = bt;
			this.c = c;
			this.s = s;
			this.i = i;
			this.l = l;
			this.f = f;
			this.d = d;
		}

	}

	enum MyEnum
	{
		EL1, EL2;
	}

	public static class AInterceptor implements AroundAction
	{
		public void beforeMethod(Method method, List<Object> arguments)
		{
			arguments.set(0, arguments.get(0) + A_INTERCEPTOR_DELTA_BEFORE);
		}

		public Object afterMethod(Object result)
		{
			return result + A_INTERCEPTOR_DELTA_AFTER;
		}

		public void beforeExceptionHandling(Method method, Throwable e)
		{
			beforeExceptionHandlingWasCalled = true;
		}

		public Object afterExceptionHandling(Object result)
		{
			return result + EX_INTERCEPTOR_DELTA;
		}
	}

	public static class DInterceptor implements AroundDispaching
	{

		public HttpServletRequest beforeExtraction(HttpServletRequest request)
		{
			if (D_INTERCEPTOR_KEY.equals(request.getParameter(ACTION_PARAM_NAME)))
			{
				((MockHttpServletRequest) request).setParameter(D_INTERCEPTOR_PARAM_NAME, D_INTERCEPTOR_PARAM_VALUE);
			}
			return request;
		}

		public String afterConverting(String result)
		{
			if (result != null && result.contains(D_INTERCEPTOR_PARAM_VALUE))
				return result + D_INTERCEPTOR_DELTA;
			return result;
		}
	}

	private static class TestEx extends RuntimeException
	{

		private static final long serialVersionUID = 992042534765982739L;

		public TestEx(String message)
		{
			super(message);
		}
	}
}