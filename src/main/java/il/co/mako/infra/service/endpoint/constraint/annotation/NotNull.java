package il.co.mako.infra.service.endpoint.constraint.annotation;

import il.co.mako.infra.service.endpoint.constraint.validators.NotNullValidator;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)

@ParamConstraintClassName(NotNullValidator.class)
public @interface NotNull
{
	String message() default "must be not null";

}
