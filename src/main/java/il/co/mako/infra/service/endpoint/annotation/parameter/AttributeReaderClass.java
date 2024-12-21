package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.AttrReader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeReaderClass
{
	Class<? extends AttrReader> value();

	String DEFAULT_NONE = "\u0000";
}
