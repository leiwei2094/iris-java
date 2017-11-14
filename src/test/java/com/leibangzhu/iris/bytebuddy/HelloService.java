package com.leibangzhu.iris.bytebuddy;

public class HelloService implements IHelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
