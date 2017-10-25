package com.freecat.container;


import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.util.Logger;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;

import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;


/**
 * container 容器
 */
public interface Container {

    public static final String ADD_VALVE_EVENT = "addValve";

    public static final String REMOVE_CHILD_EVENT = "removeChild";


    public static final String REMOVE_MAPPER_EVENT = "removeMapper";


    public static final String REMOVE_VALVE_EVENT = "removeValve";


    // ------------------------------------------------------------- Properties

    public Loader getLoader();


    public void setLoader(Loader loader);


    public Logger getLogger();



    public void setLogger(Logger logger);



    public Container getParent();


    public void setParent(Container container);


    public ClassLoader getParentClassLoader();

    public void setParentClassLoader(ClassLoader parent);

    public void addChild(Container child);

    public void addMapper(Mapper mapper);

    public void addPropertyChangeListener(PropertyChangeListener listener);


    public Container findChild(String name);


    public Container[] findChildren();


    public ContainerListener[] findContainerListeners();


    public Mapper findMapper(String protocol);

    public Mapper[] findMappers();


    public void invoke(HttpRequest request, HttpResponse response)
            throws IOException, ServletException;


    public Container map(HttpRequest request, boolean update);

    public void removeChild(Container child);

    public void removeContainerListener(ContainerListener listener);


    public void removeMapper(Mapper mapper);


    public void removePropertyChangeListener(PropertyChangeListener listener);


}
