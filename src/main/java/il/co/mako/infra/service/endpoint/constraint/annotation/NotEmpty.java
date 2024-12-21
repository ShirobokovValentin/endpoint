package il.co.mako.infra.service.endpoint.constraint.annotation;

import il.co.mako.infra.service.endpoint.constraint.validators.NotEmptyStringValidator;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)

@ParamConstraintClassName(NotEmptyStringValidator.class)
public @interface NotEmpty
{
	String message() default "must be non-empty string";

}
