package il.co.mako.infra.service.endpoint.annotation;

import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor
{
	Class<? extends AroundDispaching>[] value();
}
