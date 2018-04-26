package com.freecat.core;

import com.freecat.Loader.Loader;
import com.freecat.container.Container;

import com.freecat.container.Mapper;
import com.freecat.container.Wrapper;

import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.pipeline.Pipeline;
import com.freecat.pipeline.Valve;
import com.freecat.log.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;


public class SimpleWrapper implements Wrapper, Pipeline {

    // 存储的servlet容器
    private Servlet instance = null;
    private String servletClass;
    private Loader loader;
    private String name;
    private SimplePipeline pipeline = new SimplePipeline(this);
    protected Container parent = null;
    private Logger logger;

    public SimpleWrapper() {
        pipeline.setBasic(new SimpleWrapperValve());
    }

    public synchronized void addValve(Valve valve) {
        pipeline.addValve(valve);
    }


    //分配servlet
    public Servlet allocate() throws ServletException {

        if (instance == null) {
            try {
                instance = loadServlet();
            } catch (ServletException e) {
                throw e;
            } catch (Throwable e) {
                throw new ServletException("不能创建servlet实例", e);
            }
        }
        return instance;
    }

    private Servlet loadServlet() throws ServletException {
        if (instance != null)
            return instance;

        Servlet servlet = null;
        String actualClass = servletClass;
        if (actualClass == null) {
            throw new ServletException("servlet没有被实例化");
        }

        Loader loader = getLoader();

        //没有类加载器
        if (loader == null) {
            throw new ServletException("没有类加载器");
        }
        ClassLoader classLoader = loader.getClassLoader();


        Class classClass = null;
        try {
            if (classLoader != null) {
                classClass = classLoader.loadClass(actualClass);
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("没有找到servlet类");
        }
        // 反射加载类
        try {
            servlet = (Servlet) classClass.newInstance();
        } catch (Throwable e) {
            throw new ServletException("创建servlet失败");
        }

        // 调用servlet的初始化方法
        try {
            servlet.init(null);
        } catch (Throwable f) {
            throw new ServletException("初始化servlet失败");
        }
        return servlet;
    }

    public Loader getLoader() {
        if (loader != null)
            return (loader);
        if (parent != null)
            return (parent.getLoader());
        return (null);
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Container getParent() {
        return parent;
    }

    public void setParent(Container container) {
        parent = container;
    }

    public ClassLoader getParentClassLoader() {
        return null;
    }

    public void setParentClassLoader(ClassLoader parent) {
    }


    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public void addChild(Container child) {
    }


    public void addMapper(Mapper mapper) {
    }

    public Container findChild(String name) {
        return null;
    }

    public Container[] findChildren() {
        return null;
    }


    public void deallocate(Servlet servlet) throws ServletException {
    }


    public Mapper findMapper(String protocol) {
        return null;
    }

    public Mapper[] findMappers() {
        return null;
    }

    public void invoke(HttpRequest request, HttpResponse response)
            throws IOException, ServletException {
        pipeline.invoke(request, response);
    }


    public Container map(HttpRequest request, boolean update) {
        return null;
    }

    public void removeChild(Container child) {
    }


    public void removeMapper(Mapper mapper) {
    }


    public Valve getBasic() {
        return pipeline.getBasic();
    }

    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    public Valve[] getValves() {
        return pipeline.getValves();
    }

    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }

}