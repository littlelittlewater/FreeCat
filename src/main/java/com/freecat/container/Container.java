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
    /**添加一个Loader**/
    Loader getLoader();

    /**设置一个Loader**/
    void setLoader(Loader loader);

    /**获取一个Logger**/
    Logger getLogger();

    /**设置一个Logger**/
    void setLogger(Logger logger);

    /**获取父类的容器**/
    Container getParent();

    /**设置父类的容器**/
    void setParent(Container container);

    /**获取父类的类加载器**/
    ClassLoader getParentClassLoader();

    /**设置父类的类加载器**/
    void setParentClassLoader(ClassLoader parent);

    /**添加一个孩子**/
    void addChild(Container child);

    /**添加一个Mapper**/
    void addMapper(Mapper mapper);


    /**添加一个查找孩子**/
    Container findChild(String name);

    /**添加一个查找孩纸们**/
    Container[] findChildren();



    /**查找映射规则**/
    Mapper findMapper(String protocol);

    /**查找映射规则**/
    Mapper[] findMappers();

    /**主要的Invoke方法**/
    void invoke(HttpRequest request, HttpResponse response)
            throws IOException, ServletException;


    Container map(HttpRequest request, boolean update);

    void removeChild(Container child);


    String  getName();

    void  setName(String name);

    void removeMapper(Mapper mapper);

}
