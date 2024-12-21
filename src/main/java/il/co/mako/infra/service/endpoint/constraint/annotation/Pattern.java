package il.co.mako.infra.service.endpoint.constraint.annotation;

import il.co.mako.infra.service.endpoint.constraint.validators.PatternValidator;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)

@ParamConstraintClassName(PatternValidator.class)
@ParamConstraintFactoryMethod("getInstance")
public @interface Pattern
{
	String value();
}
