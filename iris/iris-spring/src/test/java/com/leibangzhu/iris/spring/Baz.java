package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.core.annotation.Reference;

public class Baz {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public void hello(String name) throws Exception {
        System.out.println(helloService.hello(name));
    }
}
