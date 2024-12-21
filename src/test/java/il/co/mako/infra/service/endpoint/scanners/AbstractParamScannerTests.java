package il.co.mako.infra.service.endpoint.scanners;

import il.co.mako.infra.service.AbstractTests;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;

public class AbstractParamScannerTests<T extends AbstractParamScanner<?,?>> extends AbstractTests<T>
{
	protected Inherited getSkippedAnnotation()
	{
		return new Inherited()
		{
			public Class<? extends Annotation> annotationType()
			{
				return Inherited.class;
			}
		};
	}

}
