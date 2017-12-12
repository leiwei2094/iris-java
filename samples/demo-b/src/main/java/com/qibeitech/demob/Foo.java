package com.qibeitech.demob;

import com.leibangzhu.iris.client.IRpcClient;
import com.leibangzhu.iris.demoa.api.IHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Foo {

    @Autowired
    private IRpcClient rpcClient;

    public String hello() throws Exception {
        IHelloService helloService = rpcClient.create(IHelloService.class);
        return helloService.sayHello("foo");
    }
}
