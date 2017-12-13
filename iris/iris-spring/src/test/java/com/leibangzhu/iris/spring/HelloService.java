package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.core.annotation.Service;

@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "hello" + name;
    }
}
