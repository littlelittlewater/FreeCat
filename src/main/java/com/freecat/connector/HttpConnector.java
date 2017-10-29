package com.freecat.connector;

import com.freecat.container.Container;
import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.lifecycle.Lifecycle;
import com.freecat.lifecycle.LifecycleException;
import com.freecat.lifecycle.LifecycleListener;
import com.freecat.net.DefaultServerSocketFactory;
import com.freecat.net.ServerSocketFactory;
import com.freecat.util.LifecycleSupport;
import com.freecat.log.Logger;
import com.freecat.util.StringManager;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Stack;
import java.util.Vector;


/**
 * 默认的连接器  用于处理连接
 * 1.初始化方法init--->会获取相关的socket
 * 2.启动方法start--->开启自己的线程来接受消息，并传递给processor的asign方法
 */
public final class HttpConnector
        implements Connector, Lifecycle, Runnable {


    /**
     * 接受的总数
     */
    private int acceptCount = 10;


    /**
     * 绑定的ip地址
     */
    private String address = null;


    /**
     * 输入流的输入缓存
     */
    private int bufferSize = 2048;


    /**
     * 用来处理请求的容器
     */
    protected Container container = null;


    /**
     * 创建的proccessor
     */
    private Vector created = new Vector();


    /**
     * 当前创建的proccessor总数
     */
    private int curProcessors = 0;


    /**
     * 当前的debug等级
     */
    private int debug = 0;


    /**
     * The server socket factory for this component.
     */
    private ServerSocketFactory factory = null;


    /**
     * 这个容器的生命周期支持
     */
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * 最小的处理器数目
     */
    protected int minProcessors = 5;


    /**
     * 最多的处理器数目
     */
    private int maxProcessors = 20;


    /**
     * 连接超时的默认时长
     */
    private int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;


    /**
     * 监听的端口号
     */
    private int port = 8080;


    /**
     * 空闲的processors
     */
    private Stack processors = new Stack();


    /**
     * 监听的连接
     */
    private ServerSocket serverSocket = null;


    /**
     * 日志记录器
     */
    private StringManager sm =
            StringManager.getManager(Constants.Package);


    /**
     * 容器是否被初始化
     */
    private boolean initialized = false;


    /**
     * 容器是否启动
     */
    private boolean started = false;


    /**
     * 停止后台进程的标记
     */
    private boolean stopped = false;


    /**
     * 后台进程
     */
    private Thread thread = null;


    /**
     * 后台进程的名字
     */
    private String threadName = null;


    /**
     * 容器同步对象
     */
    private Object threadSync = new Object();


    /**
     * Is chunking allowed ?
     */
    private boolean allowChunking = true;




    /**
     * Return the connection timeout for this Connector.
     */
    public int getConnectionTimeout() {

        return (connectionTimeout);

    }


    /**
     * 设置timeout时长
     */
    public void setConnectionTimeout(int connectionTimeout) {

        this.connectionTimeout = connectionTimeout;

    }


    /**
     * Return the accept count for this Connector.
     */
    public int getAcceptCount() {

        return (acceptCount);

    }


    /**
     * Set the accept count for this Connector.
     *
     * @param count The new accept count
     */
    public void setAcceptCount(int count) {

        this.acceptCount = count;

    }

    public boolean isAvailable() {

        return (started);

    }


    public int getBufferSize() {

        return (this.bufferSize);

    }

    public void setBufferSize(int bufferSize) {

        this.bufferSize = bufferSize;

    }


    /**
     * 返回处理这些请求的Container
     */
    public Container getContainer() {

        return (container);

    }


    /**
     * 设置处理这些请求的Container
     */
    public void setContainer(Container container) {

        this.container = container;

    }


    /**
     * 设置现在生成的线程数
     */
    public int getCurProcessors() {

        return (curProcessors);

    }


    /**
     * 返回当前的debug等级
     */
    public int getDebug() {

        return (debug);

    }


    /**
     * 设置denbug等级
     */
    public void setDebug(int debug) {

        this.debug = debug;

    }


    /**
     * 获取socket工厂
     */
    public ServerSocketFactory getFactory() {

        if (this.factory == null) {
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }
        return (this.factory);

    }


    /**
     * 设置容器使用socket工厂
     */
    public void setFactory(ServerSocketFactory factory) {

        this.factory = factory;

    }


    /**
     * 获取最小的处理线程数目
     */
    public int getMinProcessors() {

        return (minProcessors);

    }


    /**
     * 获取最小的处理线程数目
     */
    public void setMinProcessors(int minProcessors) {
        this.minProcessors = minProcessors;

    }


    /**
     * Return the maximum number of processors allowed, or <0 for unlimited.
     */
    public int getMaxProcessors() {

        return (maxProcessors);

    }


    /**
     * Set the maximum number of processors allowed, or <0 for unlimited.
     *
     * @param maxProcessors The new maximum processors
     */
    public void setMaxProcessors(int maxProcessors) {

        this.maxProcessors = maxProcessors;

    }


    /**
     * Return the port number on which we listen for HTTP requests.
     */
    public int getPort() {

        return (this.port);

    }


    /**
     * 设置监听端口号
     */
    public void setPort(int port) {

        this.port = port;

    }


    /**
     * 创建一个request请求
     * @return
     */
    public HttpRequest createRequest() {

        if (debug >= 2)  log("createRequest: Creating new request");
        HttpRequest request = new HttpRequest();
        request.setConnector(this);
        return (request);

    }


    public HttpResponse createResponse() {

        if (debug >= 2) log("createResponse: Creating new response");
        HttpResponse response = new HttpResponse();
        response.setConnector(this);
        return (response);

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Recycle the specified Processor so that it can be used again.
     *
     * @param processor The processor to be recycled
     */
    void recycle(HttpProcessor processor) {

        if (debug >= 2) log("recycle: Recycling processor " + processor);
        processors.push(processor);

    }


    // -------------------------------------------------------- Private Methods


    /**
     * 从栈中或者其他渠道获得一个processor
     */
    private HttpProcessor createProcessor() {

        synchronized (processors) {
            if (processors.size() > 0) {
                 if (debug >= 2) log("createProcessor: Reusing existing processor");
                return ((HttpProcessor) processors.pop());
            }
            if ((maxProcessors > 0) && (curProcessors < maxProcessors)) {
                if (debug >= 2)  log("createProcessor: Creating new processor");
                return (newProcessor());
            } else {
                if (maxProcessors < 0) {
                     if (debug >= 2) log("createProcessor: Creating new processor");
                    return (newProcessor());
                } else {
                     if (debug >= 2) log("createProcessor: Cannot create new processor");
                    return (null);
                }
            }
        }

    }


    private void log(String message) {
        Logger logger = container.getLogger();
        String localName = threadName;
        if (localName == null)
            localName = "HttpConnector";
        if (logger != null)
            logger.log(localName + " " + message);
        else
            System.out.println(localName + " " + message);

    }


    private void log(String message, Throwable throwable) {

        Logger logger = container.getLogger();
        String localName = threadName;
        if (localName == null)
            localName = "HttpConnector";
        if (logger != null)
            logger.log(localName + " " + message, throwable);
        else {
            System.out.println(localName + " " + message);
            throwable.printStackTrace(System.out);
        }

    }


    private HttpProcessor newProcessor() {

        if (debug >= 2) log("newProcessor: Creating new processor");
        HttpProcessor processor = new HttpProcessor(this, curProcessors++);
        if (processor instanceof Lifecycle) {
            try {
                processor.start();
            } catch (LifecycleException e) {
                log("newProcessor", e);
                return (null);
            }
        }
        created.addElement(processor);
        return (processor);

    }

    private ServerSocket open()
            throws IOException, KeyStoreException, NoSuchAlgorithmException,
            CertificateException, UnrecoverableKeyException,
            KeyManagementException {

        //获取factory
        ServerSocketFactory factory = getFactory();

        // If no address is specified, open a connection on all addresses
        if (address == null) {
            log(sm.getString("httpConnector.allAddresses"));
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

        // Open a server socket on the specified address
        try {
            InetAddress is = InetAddress.getByName(address);
            log(sm.getString("httpConnector.anAddress", address));
            try {
                return (factory.createSocket(port, acceptCount, is));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + address +
                        ":" + port);
            }
        } catch (Exception e) {
            log(sm.getString("httpConnector.noAddress", address));
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

    }


    // ---------------------------------------------- Background Thread Methods


    /**
     * 后台进程用于监听socke
     */
    public void run() {
        //一直循环 直到收到停止命令为止
        while (!stopped) {
            // 接受将要来到的连接
            Socket socket = null;
            try {
                if (debug >= 3)
                    log("等待连接 serverSocket.accept()");
                socket = serverSocket.accept();
                if (debug >= 3)
                    log("接受连接 serverSocket.accept()");
                if (connectionTimeout > 0)
                    socket.setSoTimeout(connectionTimeout);
            } catch (AccessControlException ace) {
                log("socket accept security exception", ace);
                continue;
            } catch (IOException e) {
                if (debug >= 3)
                    log("run: Accept returned IOException", e);
                try {
                    //如果开启socket失败 就再开一个
                    synchronized (threadSync) {
                        if (started && !stopped)
                            log("accept error: ", e);
                        if (!stopped) {
                            if (debug >= 3) log("run: Closing server socket");
                            serverSocket.close();
                            if (debug >= 3) log("run: Reopening server socket");
                            serverSocket = open();
                        }
                    }
                    if (debug >= 3) log("run: IOException processing completed");
                } catch (IOException ioe) {
                    log("socket reopen, io problem: ", ioe);
                    break;
                } catch (KeyStoreException kse) {
                    log("socket reopen, keystore problem: ", kse);
                    break;
                } catch (NoSuchAlgorithmException nsae) {
                    log("socket reopen, keystore algorithm problem: ", nsae);
                    break;
                } catch (CertificateException ce) {
                    log("socket reopen, certificate problem: ", ce);
                    break;
                } catch (UnrecoverableKeyException uke) {
                    log("socket reopen, unrecoverable key: ", uke);
                    break;
                } catch (KeyManagementException kme) {
                    log("socket reopen, key management problem: ", kme);
                    break;
                }

                continue;
            }

            // 创建一个processor来处理消息
            HttpProcessor processor = createProcessor();
            if (processor == null) {
                try {
                    log(sm.getString("httpConnector.noProcessor"));
                    socket.close();
                } catch (IOException e) {
                }
                continue;
            }
            if (debug >= 3)
                log("run: Assigning socket to processor " + processor);

            processor.assign(socket);

        }

        // Notify the threadStop() method that we have shut ourselves down
        if (debug >= 3)
            log("run: Notifying threadStop() that we have shut down");
        synchronized (threadSync) {
            threadSync.notifyAll();
        }

    }


    /**
     * Start the background processing thread.
     */
    private void threadStart() {

        log(sm.getString("httpConnector.starting"));
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();

    }


    /**
     * Stop the background processing thread.
     */
    private void threadStop() {

        log(sm.getString("httpConnector.stopping"));

        stopped = true;
        try {
            threadSync.wait(5000);
        } catch (InterruptedException e) {
        }
        thread = null;

    }


    // ------------------------------------------------------ Lifecycle Methods


    /**
     * Add a lifecycle event listener to this component.
     *
     * @param listener The listener to add
     */
    public void addLifecycleListener(LifecycleListener listener) {

        lifecycle.addLifecycleListener(listener);

    }


    /**
     * Get the lifecycle listeners associated with this lifecycle. If this
     * Lifecycle has no listeners registered, a zero-length array is returned.
     */
    public LifecycleListener[] findLifecycleListeners() {

        return lifecycle.findLifecycleListeners();

    }


    /**
     * Remove a lifecycle event listener from this component.
     *
     * @param listener The listener to add
     */
    public void removeLifecycleListener(LifecycleListener listener) {

        lifecycle.removeLifecycleListener(listener);

    }


    /**
     * 初始化连接器 （建立一个socket连接）
     */
    public void initialize()
            throws LifecycleException {
        if (initialized)
            throw new LifecycleException(
                    sm.getString("连接器已经被初始化！"));

        this.initialized = true;
        Exception eRethrow = null;

        //建立一个socket连接
        try {
            serverSocket = open();
        } catch (IOException ioe) {
            log("httpConnector, io problem: ", ioe);
            eRethrow = ioe;
        } catch (KeyStoreException kse) {
            log("httpConnector, keystore problem: ", kse);
            eRethrow = kse;
        } catch (NoSuchAlgorithmException nsae) {
            log("httpConnector, keystore algorithm problem: ", nsae);
            eRethrow = nsae;
        } catch (CertificateException ce) {
            log("httpConnector, certificate problem: ", ce);
            eRethrow = ce;
        } catch (UnrecoverableKeyException uke) {
            log("httpConnector, unrecoverable key: ", uke);
            eRethrow = uke;
        } catch (KeyManagementException kme) {
            log("httpConnector, key management problem: ", kme);
            eRethrow = kme;
        }

        if (eRethrow != null)
            throw new LifecycleException(threadName + ".open", eRethrow);

    }


    /**
     * 开始处理连接器的请求
     *
     * @throws LifecycleException if a fatal startup error occurs
     */
    public void start() throws LifecycleException {

        // 检查当期的状态
        if (started)
            throw new LifecycleException
                    (sm.getString("httpConnector.alreadyStarted"));
        threadName = "HttpConnector[" + port + "]";
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;

        // 开启后台线程
        threadStart();

        // Create the specified minimum number of processors
        while (curProcessors < minProcessors) {
            if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
                break;
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }

    }


    /**
     * Terminate processing requests via this Connector.
     *
     * @throws LifecycleException if a fatal shutdown error occurs
     */
    public void stop() throws LifecycleException {

        // Validate and update our current state
        if (!started)
            throw new LifecycleException
                    (sm.getString("httpConnector.notStarted"));
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        // Gracefully shut down all processors we have created
        for (int i = created.size() - 1; i >= 0; i--) {
            HttpProcessor processor = (HttpProcessor) created.elementAt(i);
            if (processor instanceof Lifecycle) {
                try {
                    processor.stop();
                } catch (LifecycleException e) {
                    log("HttpConnector.stop", e);
                }
            }
        }

        synchronized (threadSync) {
            // Close the server socket we were using
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
            //停止后台
            threadStop();
        }
        serverSocket = null;

    }


}

