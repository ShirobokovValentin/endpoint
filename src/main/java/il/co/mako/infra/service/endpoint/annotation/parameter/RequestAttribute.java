package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.AttributeReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AttributeReaderClass(AttributeReader.class)
public @interface RequestAttribute
{
	String value();

	String defaultValue() default ParamReaderClass.DEFAULT_NONE;
}
