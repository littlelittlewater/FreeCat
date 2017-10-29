package com.freecat.container;


import com.freecat.Loader.Loader;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.log.Logger;

import javax.servlet.ServletException;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;


/**
 * container 容器
 */
public interface Container {

    String ADD_VALVE_EVENT = "addValve";

    String REMOVE_CHILD_EVENT = "removeChild";


    String REMOVE_MAPPER_EVENT = "removeMapper";


    String REMOVE_VALVE_EVENT = "removeValve";


    Loader getLoader();


    void setLoader(Loader loader);


    Logger getLogger();



    void setLogger(Logger logger);


    Container getParent();

    void setParent(Container container);


    ClassLoader getParentClassLoader();

    void setParentClassLoader(ClassLoader parent);

    void addChild(Container child);

    void addMapper(Mapper mapper);

    void addPropertyChangeListener(PropertyChangeListener listener);


    Container findChild(String name);


    Container[] findChildren();


    ContainerListener[] findContainerListeners();


    Mapper findMapper(String protocol);

    Mapper[] findMappers();


    void invoke(HttpRequest request, HttpResponse response)
            throws IOException, ServletException;


    Container map(HttpRequest request, boolean update);

    void removeChild(Container child);

    void removeContainerListener(ContainerListener listener);


    void removeMapper(Mapper mapper);


    void removePropertyChangeListener(PropertyChangeListener listener);


    //获取名字
    String getName();

    //设置名字
    void setName(String name);
}
