package com.freecat.connector;

import com.freecat.container.Container;
import com.freecat.container.Service;
import com.freecat.lifecycle.LifecycleException;
import com.freecat.net.ServerSocketFactory;

//连接器的借口 用于连接用户的请求
public interface Connector {

    //获取container
    Container getContainer();

    //设置container
    void setContainer(Container container);


    /**
     * Return the server socket factory used by this Container.
     */
    public com.freecat.net.ServerSocketFactory getFactory();


    /**
     * Set the server socket factory used by this Container.
     *
     * @param factory The new server socket factory
     */
    public void setFactory(ServerSocketFactory factory);

    //获取连接器的信息
    String getInfo();


    /**
     * Return the port number to which a request should be redirected if
     * it comes in on a non-SSL port and is subject to a security constraint
     * with a transport guarantee that requires SSL.
     */

    //http 类型请求
    String getScheme();


    //连接器处理的请求类型
    void setScheme(String scheme);


    /**
     * Return the secure connection flag that will be assigned to requests
     * received through this connector.  Default value is "false".
     */
    public boolean getSecure();


    /**
     * Set the secure connection flag that will be assigned to requests
     * received through this connector.
     *
     * @param secure The new secure connection flag
     */
    public void setSecure(boolean secure);


    /**
     * Return the <code>Service</code> with which we are associated (if any).
     */
    public Service getService();


    /**
     * Set the <code>Service</code> with which we are associated (if any).
     *
     * @param service The service that owns this Engine
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
