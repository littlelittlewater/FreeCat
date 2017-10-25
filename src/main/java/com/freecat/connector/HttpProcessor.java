package com.freecat.connector;


import com.freecat.http.HttpRequest;
import com.freecat.http.HttpResponse;
import com.freecat.lifecycle.Lifecycle;
import com.freecat.lifecycle.LifecycleException;
import com.freecat.lifecycle.LifecycleListener;
import com.freecat.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;


/**
 * 后台进程是通过生命周期start来管理启动
 * 后台进程会在创建的时候被阻塞，除非接受到其他方法的唤醒，然后会处理socket，最后会进行回收
 */

final class HttpProcessor
    implements Lifecycle, Runnable {

    /**
     * 创建一个processor 通过id和connector
     *
     */
    public HttpProcessor(HttpConnector connector, int id) {

        super();
        this.connector = connector;
        this.debug = connector.getDebug();
        this.id = id;
        this.request = (HttpRequest) connector.createRequest();
        this.response = (HttpResponse) connector.createResponse();
        this.serverPort = connector.getPort();
        this.threadName =
          "HttpProcessor[" + connector.getPort() + "][" + id + "]"; 

    }



    /**
     * Is there a new socket available?
     */
    private boolean available = false;


    /**
     * The HttpConnector with which this processor is associated.
     */
    private HttpConnector connector = null;


    /**
     * The debugging detail level for this component.
     */
    private int debug = 0;


    /**
     * The identifier of this processor, unique per connector.
     */
    private int id = 0;


    /**
     * The lifecycle event support for this component.
     */
    private LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * The match string for identifying a session ID parameter.
     */
    private static final String match =
        ";" + Globals.SESSION_PARAMETER_NAME + "=";


    /**
     * The match string for identifying a session ID parameter.
     */
    private static final char[] SESSION_ID = match.toCharArray();


    /**
     * The string parser we will use for parsing request lines.
     */
    private StringParser parser = new StringParser();


    /**
     * The proxy server name for our Connector.
     */
    private String proxyName = null;


    /**
     * The proxy server port for our Connector.
     */
    private int proxyPort = 0;


    /**
     * The HTTP request object we will pass to our associated container.
     */
    private HttpRequest request = null;


    /**
     * The HTTP response object we will pass to our associated container.
     */
    private HttpResponse response = null;


    /**
     * The actual server port for our Connector.
     */
    private int serverPort = 0;


    /**
     * The string manager for this package.
     */
    protected StringManager sm =
        StringManager.getManager(Constants.Package);


    /**
     *
     */
    private Socket socket = null;


    /**
     * 是否开始
     */
    private boolean started = false;


    /**
     * 停止标记
     */
    private boolean stopped = false;


    /**
     * 后台线程
     */
    private Thread thread = null;

    /**
     * 线程名字
     */
    private String threadName = null;

    /**
     *  线程同步对象
     */
    private Object threadSync = new Object();

    /**
     *  处理socket消息
     */
    synchronized void assign(Socket socket) {

        // Wait for the Processor to get the previous Socket
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // Store the newly available Socket and notify our thread
        this.socket = socket;
        available = true;
        notifyAll();

        if ((debug >= 1) && (socket != null))
            log(" An incoming request is being assigned");

    }

    private synchronized Socket await() {

        // Wait for the Connector to provide a new Socket
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        Socket socket = this.socket;
        available = false;
        notifyAll();
        if ((debug >= 1) && (socket != null))
            log("  The incoming request has been awaited");
        return (socket);

    }



    private void log(String message) {

        Logger logger = connector.getContainer().getLogger();
        if (logger != null)
            logger.log(threadName + " " + message);

    }



    private void log(String message, Throwable throwable) {

        Logger logger = connector.getContainer().getLogger();
        if (logger != null)
            logger.log(threadName + " " + message, throwable);

    }


    private void process(Socket socket) {
        //处理消息
    }


    // ---------------------------------------------- Background Thread Methods


    /**
     * 处理socket重要的后台线程，初始化的时候被启动
     */
    public void run() {

        //当接受到停止的时候才停止
        while (!stopped) {
            // 等待被调用asign方法时唤醒
            Socket socket = await();
            if (socket == null)
                continue;
            // 从socket中处理信息
            try {
                process(socket);
            } catch (Throwable t) {
                log("process.invoke", t);
            }

            // 完成后自动压入栈中，用于回收
            connector.recycle(this);

        }

        // Tell threadStop() we have shut ourselves down successfully
        synchronized (threadSync) {
            threadSync.notifyAll();
        }

    }


    /**
     * 启动后台处理线程
     */
    private void threadStart() {
        log(sm.getString("httpProcessor.starting"));

        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
        if (debug >= 1)
            log(" Background thread has been started");

    }


    /**
     *  停止线程
     */
    private void threadStop() {

        log(sm.getString("httpProcessor.stopping"));

        stopped = true;
        assign(null);

        int status = 0;
        if (status != Constants.PROCESSOR_IDLE) {
            // Only wait if the processor is actually processing a command
            synchronized (threadSync) {
                try {
                    threadSync.wait(5000);
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
        thread = null;

    }


    // ------------------------------------------------------ Lifecycle Methods





    public void addLifecycleListener(LifecycleListener listener) {

        lifecycle.addLifecycleListener(listener);

    }

    public LifecycleListener[] findLifecycleListeners() {

        return lifecycle.findLifecycleListeners();

    }


    public void removeLifecycleListener(LifecycleListener listener) {

        lifecycle.removeLifecycleListener(listener);

    }


    public void start() throws LifecycleException {

        if (started)
            throw new LifecycleException
                (sm.getString("httpProcessor.alreadyStarted"));
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;
        threadStart();

    }

    public void stop() throws LifecycleException {

        if (!started)
            throw new LifecycleException
                (sm.getString("httpProcessor.notStarted"));
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        threadStop();

    }


}
