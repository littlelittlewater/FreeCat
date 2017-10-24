package com.freecat.startup;

import com.freecat.connector.HttpConnector;
import com.freecat.container.Context;
import com.freecat.container.HttpContext;
import com.freecat.lifecycle.LifecycleException;

public class BootStrap {
    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        try {
            connector.initialize();
            connector.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}