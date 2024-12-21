package il.co.mako.infra.service.endpoint.annotation.method;

import il.co.mako.infra.service.convertors.MediaType;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Representation
{
	MediaType value() default MediaType.APPLICATION_JSON;
}
