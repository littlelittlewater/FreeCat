package com.freecat.core;

import com.freecat.container.Container;
import com.freecat.container.Context;
import com.freecat.container.Wrapper;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.pipeline.Contained;
import com.freecat.pipeline.Valve;
import com.freecat.pipeline.ValveContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleContextValve implements Valve, Contained {

	protected Container container;

	public void invoke(HttpRequest request, HttpResponse response,
			ValveContext valveContext) throws IOException, ServletException {

		Context context = (Context) getContainer();
		Wrapper wrapper = null;
		try {
			wrapper = (Wrapper) context.map(request, true);
		} catch (IllegalArgumentException e) {
			badRequest(request.getRequestURL().toString(), response);
			return;
		}
		if (wrapper == null) {
			notFound(request.getRequestURL().toString(), response);
			return;
		}
		wrapper.invoke(request, response);
	}


	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	private void badRequest(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	private void notFound(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

}