package com.leiwei2094.iris.spring;

import com.leiwei2094.iris.core.IHelloService;
import com.leiwei2094.iris.core.annotation.Reference;

public class Baz {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public void hello(String name) throws Exception {
        System.out.println(helloService.hello(name));
    }
}
