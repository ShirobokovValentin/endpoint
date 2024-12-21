package il.co.mako.infra.service.endpoint.annotation;

import il.co.mako.infra.service.endpoint.interceptors.AroundDispaching;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DispatcherInterceptor
{
	Class<? extends AroundDispaching>[] value();
}
