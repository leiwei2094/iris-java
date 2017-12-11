package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.core.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class Bar {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public void run(String name) throws Exception {
        String s = helloService.hello(name);
        System.out.println(s);
    }
}
