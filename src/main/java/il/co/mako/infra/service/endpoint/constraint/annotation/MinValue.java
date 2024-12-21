package il.co.mako.infra.service.endpoint.constraint.annotation;

import il.co.mako.infra.service.endpoint.constraint.validators.MinValueValidator;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)

@ParamConstraintClassName(MinValueValidator.class)
public @interface MinValue
{
	int value(); 
	
	String message() default "custom message";
}
