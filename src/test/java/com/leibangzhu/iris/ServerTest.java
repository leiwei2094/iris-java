package com.leibangzhu.iris;

import com.leibangzhu.iris.bytebuddy.HelloService;
import com.leibangzhu.iris.bytebuddy.IHelloService;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import com.leibangzhu.iris.server.RpcServer;
import org.junit.Test;

public class ServerTest {

    @Test
    public void test() throws Exception {
        IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
        RpcServer server = new RpcServer(registry)
                .port(2000)
                .exposeService(IHelloService.class,new HelloService());
        server.run();
    }
}
