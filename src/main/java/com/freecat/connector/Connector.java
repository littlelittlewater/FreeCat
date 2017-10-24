package com.freecat.connector;

import com.freecat.container.Container;
import com.freecat.container.Service;
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
    public ServerSocketFactory getFactory();


    /**
     * 设置socketFactory
     */
    public void setFactory(ServerSocketFactory factory);


    /**
     * Return the <code>Service</code> with which we are associated (if any).
     */
    public Service getService();


    /**
     * 设置相关的servic
     */
    public void setService(Service service);


    // --------------------------------------------------------- Public Methods


     //分配一个请求
    public Request createRequest();


    //分配一个响应
    public Response createResponse();

    /**
     *
     *执行初始化，用于绑定端口好
     * @exception LifecycleException 如果已经被初始化则抛出一个生命周期异常
     */
    public void initialize()
            throws LifecycleException;
}
