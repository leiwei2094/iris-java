package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.core.IHelloService;

public class SimpleHelloService implements IHelloService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
