package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.endpoint.annotation.parameter.ParamReaderClass;
import il.co.mako.infra.service.endpoint.annotation.parameter.RequestParam;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.endpoint.exceptions.InvalidDefinitionException;
import il.co.mako.infra.service.endpoint.readers.ParamReader;
import il.co.mako.infra.service.endpoint.readers.ParameterReader;
import il.co.mako.infra.service.endpoint.scanners.annotation.DefaultValueNotPresent;
import il.co.mako.infra.service.endpoint.scanners.annotation.DefaultValueNotString;
import il.co.mako.infra.service.endpoint.scanners.annotation.Reader_DefaultValueNoneValue;
import il.co.mako.infra.service.endpoint.scanners.annotation.TestAnnotation;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParamReaderScannerTests extends AbstractParamScannerTests<ParamReaderScanner>
{
	private static final String PARAM_NAME = "param";
	
	// ------------------------------------------------------------

	@Before
	public void setUp()
	{
		testSubject = new ParamReaderScanner();
	}

	// ------------------------------------------------------------

	@Test
	public void norm()
	{
		Annotation[] annotations = { getSkippedAnnotation(), getNormalAnnotation() };
		ParamReader paramReader = testSubject.scan(annotations, null);

		assertEquals(ParameterReader.class, paramReader.getClass());
		assertEquals(PARAM_NAME, paramReader.getParamName());
	}
	

	@Test(expected = IncompatibleAnnotationException.class)
	public void multipleReaderDefined()
	{
		Annotation[] annotations = { getNormalAnnotation(), getAnnotationWithFactory() };
		testSubject.scan(annotations, null);
	}

	@Test//(expected = RequredAnnotationMissingException.class)
	public void readerMissing()
	{
		Annotation[] annotations = { getSkippedAnnotation() };
		assertNull(testSubject.scan(annotations, null));
	}

	@Test
	public void withFactory()
	{
		Annotation[] annotations = { getAnnotationWithFactory() };
		ParamReader paramReader = testSubject.scan(annotations, null);

		assertEquals(TestReader.class, paramReader.getClass());
	}

	@Test(expected = InvalidDefinitionException.class)
	public void defaultValue_notString()
	{
		Annotation[] annotations = { getDefaultValueNotString() };
		testSubject.scan(annotations, null);
	}

	@Test
	public void defaultValue_notPresent()
	{
		Annotation[] annotations = { getDefaultValueNotPresent() };
		ParamReader paramReader = testSubject.scan(annotations, null);

		assertEquals(TestReader.class, paramReader.getClass());
	}

	@Test
	public void defaultValue_specValue()
	{
		Annotation[] annotations = { getDefaultValueSpecValue() };
		ParamReader paramReader = testSubject.scan(annotations, null);

		assertEquals(TestReader.class, paramReader.getClass());
	}

	// ------------------------------------------------------------

	private RequestParam getNormalAnnotation()
	{
		return new RequestParam()
		{
			public Class<? extends Annotation> annotationType()
			{
				return RequestParam.class;
			}

			public String value()
			{
				return PARAM_NAME;
			}

			public String defaultValue()
			{
				return "defValue";
			}

		};
	}

	private TestAnnotation getAnnotationWithFactory()
	{
		return new TestAnnotation()
		{
			public Class<? extends Annotation> annotationType()
			{
				return TestAnnotation.class;
			}

		};
	}

	private DefaultValueNotString getDefaultValueNotString()
	{
		return new DefaultValueNotString()
		{
			public Class<? extends Annotation> annotationType()
			{
				return DefaultValueNotString.class;
			}

			public int defaultValue()
			{
				return 0;
			}
		};
	}

	private DefaultValueNotPresent getDefaultValueNotPresent()
	{
		return new DefaultValueNotPresent()
		{
			public Class<? extends Annotation> annotationType()
			{
				return DefaultValueNotPresent.class;
			}
		};
	}

	private Reader_DefaultValueNoneValue getDefaultValueSpecValue()
	{
		return new Reader_DefaultValueNoneValue()
		{
			public Class<? extends Annotation> annotationType()
			{
				return Reader_DefaultValueNoneValue.class;
			}

			public String defaultValue()
			{
				return ParamReaderClass.DEFAULT_NONE;
			}

		};
	}

}
