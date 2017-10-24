package com.freecat.container;

import com.freecat.connector.HttpRequestImpl;
import com.freecat.connector.HttpResponseImpl;
import com.freecat.connector.Request;
import com.freecat.connector.Response;
import com.freecat.util.Logger;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * container 容器
 */
public interface Container {


    public Logger getLogger();

    public void  invoke();

    public void invoke(Request request, Response response)
            throws IOException, ServletException;
}
