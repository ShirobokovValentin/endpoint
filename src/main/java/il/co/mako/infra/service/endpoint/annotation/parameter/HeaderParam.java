package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.HeaderReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamReaderClass(HeaderReader.class)
public @interface HeaderParam
{
	String value();

	String defaultValue() default ParamReaderClass.DEFAULT_NONE;
}
