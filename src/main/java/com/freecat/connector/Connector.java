package com.freecat.connector;

import com.freecat.container.Container;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.lifecycle.LifecycleException;
import com.freecat.net.ServerSocketFactory;

//连接器的接口 用于连接用户的请求
public interface Connector {



    //获取container
    Container getContainer();

    //设置container
    void setContainer(Container container);


    /**
     * 获取socket接口的工程
     */
    ServerSocketFactory getFactory();


    /**
     * 设置socketFactory
     */
    void setFactory(ServerSocketFactory factory);


    // --------------------------------------------------------- Public Methods


     //分配一个请求
     HttpRequest createRequest();


    //分配一个响应
    HttpResponse createResponse();

    /**
     *
     *执行初始化，用于绑定端口好
     * @exception LifecycleException 如果已经被初始化则抛出一个生命周期异常
     */
    void initialize()
            throws LifecycleException;
}
