package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.ParameterReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamReaderClass(ParameterReader.class)
public @interface RequestParam
{
	String value();
	
	String defaultValue() default ParamReaderClass.DEFAULT_NONE;
}
