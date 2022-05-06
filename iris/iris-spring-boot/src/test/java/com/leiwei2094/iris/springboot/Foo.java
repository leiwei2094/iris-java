package com.leiwei2094.iris.springboot;

import com.leiwei2094.iris.core.IHelloService;
import com.leiwei2094.iris.core.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class Foo {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public String hello(String name) throws Exception {
        return helloService.hello(name);
    }
}
