package com.freecat.connector;

import com.freecat.container.Context;
import com.freecat.container.Wrapper;
import com.freecat.util.ParameterMap;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.util.*;

public class MyHttpRequest implements HttpRequest, HttpServletRequest {
    //存储cookie的集合
    protected ArrayList cookies = new ArrayList();

    //存储header的集合
    protected HashMap headers = new HashMap();

    //局部相关的量
    protected ArrayList locales = new ArrayList();

    //局部变量  加锁的一个安全变量  这个锁只能用来拍错 不能用于程序判定
    protected ParameterMap parameters = null;

    protected String contextPath = "";

    private String authType;

    private String method = null;

    private String queryString = null;

    //添加一个cookie
    public void addCookie(Cookie cookie) {
        synchronized (cookies) {
            cookies.add(cookie);
        }

    }

    //添加一个header
    public void addHeader(String name, String value) {
        name = name.toLowerCase();
        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values == null) {
                values = new ArrayList();
                headers.put(name, values);
            }
            values.add(value);
        }
    }

    //添加局部变量
    public void addLocale(Locale locale) {
        synchronized (locales) {
            locales.add(locale);
        }

    }

    //添加一个变量
    public void addParameter(String name, String[] values) {
        synchronized (parameters) {
            parameters.put(name, values);
        }
    }

    //清除cookie
    public void clearCookies() {
        synchronized (cookies) {
            cookies.clear();
        }

    }

    public void clearHeaders() {
        headers.clear();
    }

    public void clearLocales() {
        locales.clear();
    }


    public void clearParameters() {
        if (parameters != null) {
            parameters.setLocked(false);
            parameters.clear();
        } else {
            parameters = new ParameterMap();
        }
    }

    public void setAuthType(String type) {
        this.authType = authType;
    }

    public void setContextPath(String path) {
        if (path == null)
            this.contextPath = "";
        else
            this.contextPath = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setQueryString(String query) {
        this.queryString = query;
    }

    public void setPathInfo(String path) {

    }

    public void setRequestedSessionCookie(boolean flag) {

    }

    public void setRequestedSessionId(String id) {

    }

    public void setRequestedSessionURL(boolean flag) {

    }

    public void setRequestURI(String uri) {

    }

    public void setDecodedRequestURI(String uri) {

    }

    public String getDecodedRequestURI() {
        return null;
    }

    public void setServletPath(String path) {

    }

    public void setUserPrincipal(Principal principal) {

    }

    public String getAuthorization() {
        return null;
    }

    public void setAuthorization(String authorization) {

    }

    public Connector getConnector() {
        return null;
    }

    public void setConnector(Connector connector) {

    }

    public Context getContext() {
        return null;
    }

    public void setContext(Context context) {

    }

    public String getInfo() {
        return null;
    }

    public ServletRequest getRequest() {
        return null;
    }

    public Response getResponse() {
        return null;
    }

    public void setResponse(Response response) {

    }

    public Socket getSocket() {
        return null;
    }

    public void setSocket(Socket socket) {

    }

    public InputStream getStream() {
        return null;
    }

    public void setStream(InputStream stream) {

    }

    public Wrapper getWrapper() {
        return null;
    }

    public void setWrapper(Wrapper wrapper) {

    }

    public ServletInputStream createInputStream() throws IOException {
        return null;
    }

    public void finishRequest() throws IOException {

    }

    public Object getNote(String name) {
        return null;
    }

    public Iterator getNoteNames() {
        return null;
    }

    public void recycle() {

    }

    public void removeNote(String name) {

    }

    public void setContentLength(int length) {

    }

    public void setContentType(String type) {

    }

    public void setNote(String name, Object value) {

    }

    public void setProtocol(String protocol) {

    }

    public void setRemoteAddr(String remote) {

    }

    public void setScheme(String scheme) {

    }

    public void setSecure(boolean secure) {

    }

    public void setServerName(String name) {

    }

    public void setServerPort(int port) {

    }

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    public long getDateHeader(String s) {
        return 0;
    }

    public String getHeader(String s) {
        return null;
    }

    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    public Enumeration<String> getHeaderNames() {
        return null;
    }

    public int getIntHeader(String s) {
        return 0;
    }

    public String getMethod() {
        return null;
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole(String s) {
        return false;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return null;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getServletPath() {
        return null;
    }

    public HttpSession getSession(boolean b) {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    public void login(String s, String s1) throws ServletException {

    }

    public void logout() throws ServletException {

    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    public Object getAttribute(String s) {
        return null;
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public String getParameter(String s) {
        return null;
    }

    public Enumeration<String> getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String s) {
        return new String[0];
    }

    public Map<String, String[]> getParameterMap() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public void setAttribute(String s, Object o) {

    }

    public void removeAttribute(String s) {

    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration<Locale> getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public String getRealPath(String s) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    public boolean isAsyncStarted() {
        return false;
    }

    public boolean isAsyncSupported() {
        return false;
    }

    public AsyncContext getAsyncContext() {
        return null;
    }

    public DispatcherType getDispatcherType() {
        return null;
    }

    public void setInet(InetAddress inet) {
        
    }

    public HttpHeader allocateHeader() {
        return null;
    }

    public void nextHeader() {
    }
}
