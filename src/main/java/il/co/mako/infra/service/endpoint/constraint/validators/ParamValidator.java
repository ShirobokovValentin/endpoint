package il.co.mako.infra.service.endpoint.constraint.validators;

import il.co.mako.infra.service.endpoint.constraint.entity.Violation;

public interface ParamValidator
{
	public Violation check(Object obj);
	
	public void setMessage(String message);

}
