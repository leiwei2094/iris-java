package com.leibangzhu.iris.demoa.service.service;

import com.leibangzhu.iris.demoa.api.IHelloService;

public class HelloService implements IHelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    public String sayHello2(String name) {
        return "Hello, " + name;
    }

}
