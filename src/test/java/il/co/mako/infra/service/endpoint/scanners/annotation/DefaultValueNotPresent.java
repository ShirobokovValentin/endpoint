package il.co.mako.infra.service.endpoint.scanners.annotation;

import il.co.mako.infra.service.endpoint.annotation.parameter.ParamReaderClass;
import il.co.mako.infra.service.endpoint.scanners.TestReader;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamReaderClass(TestReader.class)
public @interface DefaultValueNotPresent
{

}
