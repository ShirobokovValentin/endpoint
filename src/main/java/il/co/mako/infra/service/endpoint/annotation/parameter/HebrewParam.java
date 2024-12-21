package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.convertors.PossiblyHebrewConvertor;
import il.co.mako.infra.service.endpoint.readers.ParameterReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamReaderClass(ParameterReader.class)
@ParamConvertorClass(PossiblyHebrewConvertor.class)
public @interface HebrewParam
{
	String value();
}
