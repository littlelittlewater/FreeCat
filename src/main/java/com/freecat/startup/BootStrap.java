package com.freecat.startup;

import com.freecat.connector.HttpConnector;
import com.freecat.core.SimpleLoader;
import com.freecat.core.SimpleWrapper;
import com.freecat.container.Wrapper;
import com.freecat.lifecycle.LifecycleException;
import com.freecat.log.Logger;
import com.freecat.log.SystemOutLogger;

import java.io.IOException;

public class BootStrap {
    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.setDebug(Logger.DEBUG);
        Wrapper wrapper = new SimpleWrapper();
        wrapper.setServletClass("PrimitiveServlet");
        wrapper.setLoader(new SimpleLoader());
        Logger logger = new SystemOutLogger();
        wrapper.setLogger(logger);
        connector.setContainer(wrapper);
        try {
            connector.initialize();
            connector.start();
            System.in.read();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}