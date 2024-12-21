package il.co.mako.infra.service.endpoint.constraint.validators;

public abstract class AbstractValidator implements ParamValidator
{
	protected String message;

	// ------------------------------------------------------------

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return this.message;
	}

}
