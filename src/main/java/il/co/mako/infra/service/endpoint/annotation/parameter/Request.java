package il.co.mako.infra.service.endpoint.annotation.parameter;

import il.co.mako.infra.service.endpoint.readers.RawRequest;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AttributeReaderClass(RawRequest.class)
public @interface Request
{
}
