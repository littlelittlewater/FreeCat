package com.freecat.container;

/***
 * context容器
 */


public interface Context extends Container{
    void addServletMapping(String s, String primitive);
}