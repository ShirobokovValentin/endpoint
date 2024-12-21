package il.co.mako.infra.service.endpoint.readers;

import javax.servlet.http.HttpServletRequest;

public interface ParamReader
{
	public String read(HttpServletRequest request);

	public void setDefaultValue(String defaultValue);
	
	public String getParamName();
}
