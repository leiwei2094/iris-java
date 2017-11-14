package com.leibangzhu.iris.client;

import com.leibangzhu.iris.core.IHelloService;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientTest {

    @Test
    public void test() throws Exception {
        IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
        RpcClient client = new RpcClient(registry);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    IHelloService helloService = client.create(IHelloService.class);
                    String s = helloService.hello("leo");
                    System.out.println("====" + s);

                    String s2 = helloService.hello("tom");
                    System.out.println("====" + s2);

                    String s3 = helloService.hello("jerry");
                    System.out.println("====" + s3);

                    System.out.println("==== rpc invoke finished...");
                    Thread.sleep(2 * 1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },5,3, TimeUnit.SECONDS);

        Thread.sleep(3000 * 1000);
    }
}
