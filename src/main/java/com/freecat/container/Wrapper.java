
package com.freecat.container;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

public interface Wrapper extends Container {


    void setServletClass(String servletClass);

    Servlet allocate() throws ServletException;

    void deallocate(Servlet servlet) throws ServletException;



}
