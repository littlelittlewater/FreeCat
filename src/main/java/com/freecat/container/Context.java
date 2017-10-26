package com.freecat.container;

import com.freecat.util.CharsetMapper;

import javax.servlet.ServletContext;

/**
 * Created by liyuan on 17-10-18.
 */
public interface Context extends Container{
    void addServletMapping(String s, String primitive);
}