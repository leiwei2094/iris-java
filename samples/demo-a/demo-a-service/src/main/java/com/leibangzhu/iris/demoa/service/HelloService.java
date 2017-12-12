package com.leibangzhu.iris.demoa.service;

import com.leibangzhu.iris.demoa.api.IHelloService;

public class HelloService implements IHelloService {
    @Override
    public String sayHello(String name) {
        return "hello" + name;
    }

    @Override
    public String sayHello2(String name) {
        return "hello" + name;
    }
}
