package il.co.mako.infra.service.endpoint.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dispatcher
{
	String value();
}
