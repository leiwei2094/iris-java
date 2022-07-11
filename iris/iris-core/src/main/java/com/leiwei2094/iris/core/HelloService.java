package com.leiwei2094.iris.core;

/**
 * @author wei.lei
 */
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        Thread.sleep(2 * 1000);
        return "Hello, " + name;
    }
}
