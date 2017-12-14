package com.leibangzhu.iris.springboot;

import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.core.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class Foo {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public String hello(String name) throws Exception {
        return helloService.hello(name);
    }
}
