package il.co.mako.infra.service.endpoint.scanners.annotation;

import il.co.mako.infra.service.endpoint.annotation.parameter.ParamConvertorClass;
import il.co.mako.infra.service.endpoint.annotation.parameter.ParamConvertorFactoryMethod;
import il.co.mako.infra.service.endpoint.convertors.HebrewConvertor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParamConvertorClass(HebrewConvertor.class)
@ParamConvertorFactoryMethod("getInstance")
public @interface Convertor_FactoryMethodPresent
{

}
