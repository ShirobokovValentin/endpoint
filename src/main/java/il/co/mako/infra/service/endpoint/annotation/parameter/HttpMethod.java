package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.HttpMethodReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamReaderClass(HttpMethodReader.class)
public @interface HttpMethod
{
}
