package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.convertors.ParamConvertor;
import il.co.mako.infra.service.endpoint.convertors.HebrewConvertor;
import il.co.mako.infra.service.endpoint.exceptions.IncompatibleAnnotationException;
import il.co.mako.infra.service.endpoint.scanners.annotation.Convertor_FactoryMethodMissing;
import il.co.mako.infra.service.endpoint.scanners.annotation.Convertor_FactoryMethodPresent;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParamConvertorScannerTests extends AbstractParamScannerTests<ParamConvertorScanner>
{
	@Before
	public void setUp()
	{
		testSubject = new ParamConvertorScanner();
	}

	// ------------------------------------------------------------

	@Test
	public void convertorNotDefined()
	{
		Annotation[] annotations = { getSkippedAnnotation() };
		ParamConvertor<?> paramConvertor = testSubject.scan(annotations, Integer.class);

		assertNotNull(paramConvertor.getClass());
	}

	@Test
	public void factoryMethodMissing()
	{
		Annotation[] annotations = { getSkippedAnnotation(), getFactoryMethodMissing() };
		ParamConvertor<?> paramConvertor = testSubject.scan(annotations, Integer.class);

		assertEquals(HebrewConvertor.class, paramConvertor.getClass());
	}

	@Test
	public void factoryMethodPresent()
	{
		Annotation[] annotations = { getSkippedAnnotation(), getFactoryMethodPresent() };
		ParamConvertor<?> paramConvertor = testSubject.scan(annotations, Integer.class);

		assertEquals(HebrewConvertor.class, paramConvertor.getClass());
	}

	@Test(expected = IncompatibleAnnotationException.class)
//	@Ignore
	public void multipleConvertorDefined()
	{
		Annotation[] annotations = { getFactoryMethodMissing(), getFactoryMethodPresent() };
		testSubject.scan(annotations, Integer.class);
	}

	// ------------------------------------------------------------

	private Convertor_FactoryMethodMissing getFactoryMethodMissing()
	{
		return new Convertor_FactoryMethodMissing()
		{
			public Class<? extends Annotation> annotationType()
			{
				return Convertor_FactoryMethodMissing.class;
			}
		};
	}

	private Convertor_FactoryMethodPresent getFactoryMethodPresent()
	{
		return new Convertor_FactoryMethodPresent()
		{
			public Class<? extends Annotation> annotationType()
			{
				return Convertor_FactoryMethodPresent.class;
			}
		};
	}

}
