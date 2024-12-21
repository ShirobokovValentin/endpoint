package il.co.mako.infra.service.endpoint.readers;

import javax.servlet.http.HttpServletRequest;

public class HttpMethodReader extends WithParamReader
{
	public String read(HttpServletRequest request)
	{
		return request.getMethod();
	}

}
