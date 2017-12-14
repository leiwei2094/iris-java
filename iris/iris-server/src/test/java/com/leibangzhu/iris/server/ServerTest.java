package com.leibangzhu.iris.server;


import com.leibangzhu.iris.core.HelloService;
import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ServerTest {

    @Test
    public void test() throws Exception {
        IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
        RpcServer server = new RpcServer(registry)
                .port(2017)
                .exposeService(IHelloService.class,new HelloService());
        server.run();
        Thread.sleep(100 * 1000);
    }
}
