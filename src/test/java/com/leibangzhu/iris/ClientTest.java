package com.leibangzhu.iris;

import com.leibangzhu.iris.bytebuddy.IHelloService;
import com.leibangzhu.iris.client.RpcClient;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import org.junit.Test;

public class ClientTest {

    @Test
    public void test() throws Exception {
        IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
        RpcClient client = new RpcClient(registry);

        IHelloService helloService = client.create(IHelloService.class);
        String s = helloService.hello("leo");
        System.out.println("====" + s);

        String s2 = helloService.hello("tom");
        System.out.println("====" + s2);

        String s3 = helloService.hello("jerry");
        System.out.println("====" + s3);

        System.out.println("==== rpc invoke finished...");
    }
}
