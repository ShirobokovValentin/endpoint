package il.co.mako.infra.service.endpoint.annotation;

import il.co.mako.infra.service.endpoint.interceptors.AroundAction;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionInterceptor
{
	Class<? extends AroundAction>[] value();
}
