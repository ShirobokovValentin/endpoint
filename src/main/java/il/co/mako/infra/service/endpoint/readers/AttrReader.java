package il.co.mako.infra.service.endpoint.readers;

import javax.servlet.http.HttpServletRequest;

public interface AttrReader
{
	public Object read(HttpServletRequest request);

	public String getParamName();
}
