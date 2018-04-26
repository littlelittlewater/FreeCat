package com.freecat.core;

import com.freecat.container.Container;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.pipeline.Contained;
import com.freecat.pipeline.Pipeline;
import com.freecat.pipeline.Valve;
import com.freecat.pipeline.ValveContext;

import javax.servlet.ServletException;
import java.io.IOException;

public class SimplePipeline implements Pipeline {

  public SimplePipeline(Container container) {
    setContainer(container);
  }

  /** 最基础的运行阀 **/
  protected Valve basic = null;
  /** 容器 **/
  protected Container container = null;
  /** 需要执行的过滤器序列 **/
  protected Valve valves[] = new Valve[0];

  public void setContainer(Container container) {
    this.container = container;
  }

  public Valve getBasic() {
    return basic;
  }

  public void setBasic(Valve valve) {
    this.basic = valve;
    ((Contained) valve).setContainer(container);
  }

  public void addValve(Valve valve) {
    if (valve instanceof Contained)
      ((Contained) valve).setContainer(this.container);

    synchronized (valves) {
      Valve results[] = new Valve[valves.length +1];
      System.arraycopy(valves, 0, results, 0, valves.length);
      results[valves.length] = valve;
      valves = results;
    }
  }

  public Valve[] getValves() {
    return valves;
  }

  public void invoke(HttpRequest request, HttpResponse response)
    throws IOException, ServletException {
     /** 通过context定义的方法来执行  **/
    (new SimplePipelineValveContext()).invokeNext(request, response);
  }

  public void removeValve(Valve valve) {
  }

  /**
   * 迭代
   */
  protected class SimplePipelineValveContext implements ValveContext {

    protected int stage = 0;



    public void invokeNext(HttpRequest request, HttpResponse response)
      throws IOException, ServletException {
      int subscript = stage;
      stage = stage + 1;
      // 通过数组依次执行
      if (subscript < valves.length) {
        valves[subscript].invoke(request, response, this);
      }
      else if ((subscript == valves.length) && (basic != null)) {
        basic.invoke(request, response, this);
      }
      else {
        throw new ServletException("No valve");
      }
    }
  }

}