package il.co.mako.infra.service.endpoint.entity;

import il.co.mako.infra.service.convertors.MediaType;
import il.co.mako.infra.service.endpoint.interceptors.AroundAction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActionDescriptor extends AbstractMethodDescriptor
{
	private MediaType mediaType = MediaType.APPLICATION_JSON;
	
	private List<ArgumentDescriptor> argumentDescriptors;

	private List<AroundAction> interceptors;

	// ------------------------------------------------------------

	public ActionDescriptor()
	{
	}

	public ActionDescriptor(Method method)
	{
		super(method);
	}

	public ActionDescriptor(Method method, MediaType mediaType)
	{
		this(method);
		this.mediaType = mediaType;
	}

	// ------------------------------------------------------------

	public MediaType getMediaType()
	{
		return this.mediaType;
	}

	public void setMediaType(MediaType mediaType)
	{
		this.mediaType = mediaType;
	}

	public List<AroundAction> getInterceptors()
	{
		return this.interceptors;
	}

	public void addInterceptor(AroundAction newInstance)
	{
		if (this.interceptors == null)
			this.interceptors = new ArrayList<AroundAction>();
		this.interceptors.add(newInstance);

	}

	public List<ArgumentDescriptor> getArgumentDescriptors()
	{
		return this.argumentDescriptors;
	}

	public void setArgumentDescriptors(List<ArgumentDescriptor> argumentDescriptor)
	{
		this.argumentDescriptors = argumentDescriptor;
	}

}
