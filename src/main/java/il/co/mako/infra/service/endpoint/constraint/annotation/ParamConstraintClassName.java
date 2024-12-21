package il.co.mako.infra.service.endpoint.constraint.annotation;

import il.co.mako.infra.service.endpoint.constraint.validators.ParamValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamConstraintClassName
{
	Class<? extends ParamValidator> value();
}
