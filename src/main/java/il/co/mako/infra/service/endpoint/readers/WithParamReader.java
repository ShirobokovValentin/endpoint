package il.co.mako.infra.service.endpoint.readers;

import il.co.mako.infra.service.endpoint.annotation.parameter.ParamReaderClass;

public abstract class WithParamReader implements ParamReader
{
	private String defaultValue;
	private String params;

	// ------------------------------------------------------------

	public String getDefaultValue()
	{
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		if (ParamReaderClass.DEFAULT_NONE.equals(defaultValue))
			this.defaultValue = null;
		else
			this.defaultValue = defaultValue;
	}

	public String getParams()
	{
		return this.params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	public String getParamName()
	{
		return getParams();
	}

}
