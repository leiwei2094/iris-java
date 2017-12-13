package com.leibangzhu.iris.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlClient {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans-client.xml");
        Baz baz = context.getBean(Baz.class);

        baz.hello("leo");
        baz.hello("tom");

//        server.port(2017);
//        server.run();

        Thread.sleep(100 * 1000);

    }
}
