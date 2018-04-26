package com.freecat.core;

import com.freecat.Loader.Loader;
import com.freecat.container.Container;
import com.freecat.container.Context;
import com.freecat.container.Mapper;
import com.freecat.container.Wrapper;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.log.Logger;
import com.freecat.pipeline.Pipeline;
import com.freecat.pipeline.Valve;
import com.freecat.util.CharsetMapper;

import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

public class SimpleContext implements Context, Pipeline {

  public SimpleContext() {
    pipeline.setBasic(new SimpleContextValve());
  }

  protected HashMap children = new HashMap();
  protected Loader loader = null;
  protected SimplePipeline pipeline = new SimplePipeline(this);
  protected HashMap servletMappings = new HashMap();
  protected Mapper mapper = null;
  protected HashMap mappers = new HashMap();
  private Container parent = null;



  public void addServletMapping(String pattern, String name) {
    synchronized (servletMappings) {
      servletMappings.put(pattern, name);
    }
  }

  public String findServletMapping(String pattern) {
    synchronized (servletMappings) {
      return ((String) servletMappings.get(pattern));
    }
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
    return null;
  }

  public void setLogger(Logger logger) {
  }



  public String getName() {
    return null;
  }

  public void setName(String name) {
  }

  public Container getParent() {
    return null;
  }

  public void setParent(Container container) {
  }

  public ClassLoader getParentClassLoader() {
    return null;
  }

  public void setParentClassLoader(ClassLoader parent) {
  }




  public void addChild(Container child) {
    child.setParent(this);
    children.put(child.getName(), child);
  }



  public void addMapper(Mapper mapper) {

    mapper.setContainer(this);
    this.mapper = mapper;

  }


  public Container findChild(String name) {
    if (name == null)
      return (null);
    synchronized (children) {       // Required by post-start changes
      return ((Container) children.get(name));
    }
  }

  public Container[] findChildren() {
    synchronized (children) {
      Container results[] = new Container[children.size()];
      return ((Container[]) children.values().toArray(results));
    }
  }


  public Mapper findMapper(String protocol) {

    if (mapper != null)
      return (mapper);
    else
      synchronized (mappers) {
        return ((Mapper) mappers.get(protocol));
      }
  }

  public Mapper[] findMappers() {
    return null;
  }

  public void invoke(HttpRequest request, HttpResponse response)
    throws IOException, ServletException {
    pipeline.invoke(request, response);
  }

  public Container map(HttpRequest request, boolean update) {

    Mapper mapper = findMapper(request.getProtocol());
    if (mapper == null)
      return (null);
    return (mapper.map(request, update));
  }

  public void removeChild(Container child) {
  }


  public void removeMapper(Mapper mapper) {
  }


  // method implementations of Pipeline
  public Valve getBasic() {
    return pipeline.getBasic();
  }

  public void setBasic(Valve valve) {
    pipeline.setBasic(valve);
  }

  public synchronized void addValve(Valve valve) {
    pipeline.addValve(valve);
  }

  public Valve[] getValves() {
    return pipeline.getValves();
  }

  public void removeValve(Valve valve) {
    pipeline.removeValve(valve);
  }

}