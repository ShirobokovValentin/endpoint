package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.convertors.ParamConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamConvertorClass
{
	Class<? extends ParamConvertor<?>> value();
}
