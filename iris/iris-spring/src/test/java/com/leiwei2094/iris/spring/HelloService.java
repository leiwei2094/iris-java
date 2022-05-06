package com.leiwei2094.iris.spring;

import com.leiwei2094.iris.core.IHelloService;
import com.leiwei2094.iris.core.annotation.Service;

@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "hello" + name;
    }
}
