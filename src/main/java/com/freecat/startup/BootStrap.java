package com.freecat.startup;

import com.freecat.Loader.Loader;
import com.freecat.connector.HttpConnector;
import com.freecat.container.Context;
import com.freecat.container.Mapper;
import com.freecat.core.SimpleContext;
import com.freecat.core.SimpleContextMapper;
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
        Logger logger = new SystemOutLogger();
        Loader loader = new SimpleLoader();
        connector.setDebug(Logger.DEBUG);

        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);


        Mapper mapper = new SimpleContextMapper();
        context.addMapper(mapper);
        context.setLoader(loader);
        // context.addServletMapping(pattern, name);
        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");


        connector.setContainer(context);
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