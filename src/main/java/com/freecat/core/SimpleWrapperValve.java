package com.freecat.core;

import com.freecat.container.Container;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.pipeline.Contained;
import com.freecat.pipeline.Valve;
import com.freecat.pipeline.ValveContext;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

public class SimpleWrapperValve implements Valve, Contained {

	protected Container container;

	public void invoke(HttpRequest request, HttpResponse response,
					   ValveContext valveContext) throws IOException  {

		SimpleWrapper wrapper = (SimpleWrapper) getContainer();
		Servlet servlet = null;

		try {
			servlet = wrapper.allocate();
			if (request != null && response != null) {
				servlet.service(request, response);
			} else {
				servlet.service(request, response);
			}
		} catch (ServletException e) {
			System.err.println(e);
		}
	}


	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}
}