package il.co.mako.infra.service.convertors;

import il.co.mako.infra.service.convertors.exceptions.TypeConversionException;

public interface ParamConvertor<T>
{
	public T fromString(String paramValue) throws TypeConversionException;
}
