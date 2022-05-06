package com.leiwei2094.iris.springboot;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(value = "classpath:/application-client.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
public class ClientTest {

    @Autowired
    private Foo foo;

    @Ignore
    @Test
    public void test() throws Exception {
        System.out.println("==tom==");
        System.out.println(foo.hello("tom"));
        System.out.println("==leo==");
        System.out.println(foo.hello("leo"));
    }
}
