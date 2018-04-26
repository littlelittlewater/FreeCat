package com.freecat.container;


/***
 * context容器
 */

public interface Context extends Container{
    /**
     * 添加映射
     * @param s
     * @param primitive
     */
    void addServletMapping(String s, String primitive);
}