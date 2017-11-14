package com.leibangzhu.iris.bytebuddy;

public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        Thread.sleep(2 * 1000);
        return "Hello, " + name;
    }
}
