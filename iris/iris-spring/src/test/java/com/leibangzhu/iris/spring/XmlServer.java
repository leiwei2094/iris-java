package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.server.RpcServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlServer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans-server.xml");
        RpcServer server = context.getBean(RpcServer.class);
        server.port(2017);
        server.run();

        Thread.sleep(100 * 1000);
    }
}
