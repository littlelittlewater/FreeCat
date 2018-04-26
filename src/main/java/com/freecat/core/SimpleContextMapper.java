package com.freecat.core;

import com.freecat.container.Container;
import com.freecat.container.Mapper;
import com.freecat.container.Wrapper;
import com.freecat.http.HttpRequest;


public class SimpleContextMapper implements Mapper {

	private SimpleContext context = null;

	public Container getContainer() {
		return (context);
	}

	public void setContainer(Container container) {
		if (!(container instanceof SimpleContext))
			throw new IllegalArgumentException("不合法的参数");
		context = (SimpleContext) container;
	}


	public Container map(HttpRequest request, boolean update) {
		String contextPath =request.getContextPath();
		String requestURI = request.getRequestURI();
		String relativeURI = requestURI.substring(contextPath.length());
		Wrapper wrapper = null;
		String name = context.findServletMapping(relativeURI);
		if (name != null)
			wrapper = (Wrapper) context.findChild(name);
		return (wrapper);
	}
}