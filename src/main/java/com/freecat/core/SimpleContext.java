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

  public Object[] getApplicationListeners() {
    return null;
  }

  public void setApplicationListeners(Object listeners[]) {
  }

  public boolean getAvailable() {
    return false;
  }

  public void setAvailable(boolean flag) {
  }

  public CharsetMapper getCharsetMapper() {
    return null;
  }

  public void setCharsetMapper(CharsetMapper mapper) {
  }

  public boolean getConfigured() {
    return false;
  }

  public void setConfigured(boolean configured) {
  }

  public boolean getCookies() {
    return false;
  }

  public void setCookies(boolean cookies) {
  }

  public boolean getCrossContext() {
    return false;
  }

  public void setCrossContext(boolean crossContext) {
  }

  public String getDisplayName() {
    return null;
  }

  public void setDisplayName(String displayName) {
  }

  public boolean getDistributable() {
    return false;
  }

  public void setDistributable(boolean distributable) {
  }

  public String getDocBase() {
    return null;
  }

  public void setDocBase(String docBase) {
  }


  public String getPath() {
    return null;
  }

  public void setPath(String path) {
  }

  public String getPublicId() {
    return null;
  }

  public void setPublicId(String publicId) {
  }

  public boolean getReloadable() {
    return false;
  }

  public void setReloadable(boolean reloadable) {
  }

  public boolean getOverride() {
    return false;
  }

  public void setOverride(boolean override) {
  }

  public boolean getPrivileged() {
    return false;
  }

  public void setPrivileged(boolean privileged) {
  }

  public ServletContext getServletContext() {
    return null;
  }

  public int getSessionTimeout() {
    return 0;
  }

  public void setSessionTimeout(int timeout) {
  }

  public String getWrapperClass() {
    return null;
  }

  public void setWrapperClass(String wrapperClass) {
  }

  public void addApplicationListener(String listener) {
  }

  public void addInstanceListener(String listener) {
  }

  public void addMimeMapping(String extension, String mimeType) {
  }

  public void addParameter(String name, String value) {
  }


  public void addResourceEnvRef(String name, String type) {
  }

  public void addRoleMapping(String role, String link) {
  }

  public void addSecurityRole(String role) {
  }

  public void addServletMapping(String pattern, String name) {
    synchronized (servletMappings) {
      servletMappings.put(pattern, name);
    }
  }

  public void addTaglib(String uri, String location) {
  }

  public void addWelcomeFile(String name) {
  }

  public void addWrapperLifecycle(String listener) {
  }

  public void addWrapperListener(String listener) {
  }

  public Wrapper createWrapper() {
    return null;
  }

  public String[] findApplicationListeners() {
    return null;
  }
  public String findServletMapping(String pattern) {
    synchronized (servletMappings) {
      return ((String) servletMappings.get(pattern));
    }
  }

  public String[] findServletMappings() {
    return null;
  }

  public void reload() {
  }

  public void removeApplicationListener(String listener) {
  }

  public void removeApplicationParameter(String name) {
  }

  public void removeEjb(String name) {
  }

  public void removeEnvironment(String name) {
  }

  public void removeInstanceListener(String listener) {
  }

  public void removeLocalEjb(String name) {
  }

  public void removeMimeMapping(String extension) {
  }

  public void removeParameter(String name) {
  }

  public void removeResource(String name) {
  }

  public void removeResourceEnvRef(String name) {
  }

  public void removeResourceLink(String name) {
  }

  public void removeRoleMapping(String role) {
  }

  public void removeSecurityRole(String role) {
  }

  public void removeServletMapping(String pattern) {
  }

  public void removeTaglib(String uri) {
  }

  public void removeWelcomeFile(String name) {
  }

  public void removeWrapperLifecycle(String listener) {
  }

  public void removeWrapperListener(String listener) {
  }


  //methods of the Container interface
  public String getInfo() {
    return null;
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


  public DirContext getResources() {
    return null;
  }

  public void setResources(DirContext resources) {
  }

  public void addChild(Container child) {
    child.setParent(this);
    children.put(child.getName(), child);
  }

  public void addContainerListener(ContainerListener listener) {
  }

  public void addMapper(Mapper mapper) {
    // this method is adopted from addMapper in ContainerBase
    // the first mapper added becomes the default mapper
    mapper.setContainer(this);      // May throw IAE
    this.mapper = mapper;
    synchronized(mappers) {
      if (mappers.get(mapper.getProtocol()) != null)
        throw new IllegalArgumentException("addMapper:  Protocol '" +
          mapper.getProtocol() + "' is not unique");
      mapper.setContainer(this);      // May throw IAE
      mappers.put(mapper.getProtocol(), mapper);
      if (mappers.size() == 1)
        this.mapper = mapper;
      else
        this.mapper = null;
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
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

  public ContainerListener[] findContainerListeners() {
    return null;
  }

  public Mapper findMapper(String protocol) {
    // the default mapper will always be returned, if any,
    // regardless the value of protocol
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

  public void removeContainerListener(ContainerListener listener) {
  }

  public void removeMapper(Mapper mapper) {
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
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