package il.co.mako.infra.service.endpoint.interceptors;

import javax.servlet.http.HttpServletRequest;

public interface AroundDispaching
{
	public HttpServletRequest beforeExtraction(HttpServletRequest request);
	
	public String afterConverting(String result);

}
